package com.ferroli.after_sales.salesAppoint

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest
import com.ferroli.after_sales.R
import com.ferroli.after_sales.entity.*
import com.ferroli.after_sales.utils.DateDeserializer
import com.ferroli.after_sales.utils.LoginInfo
import com.google.gson.GsonBuilder
import org.json.JSONArray
import org.json.JSONObject
import java.text.FieldPosition
import java.text.SimpleDateFormat
import java.util.*

class SalesAppointOperationViewModel(application: Application) : AndroidViewModel(application) {
    private var _detailInfoRecord = MutableLiveData<List<DetailInfo>?>()
    private var _soLineRecord = MutableLiveData<List<SalesOrderLine>?>()
    private var _appointUserRecord = MutableLiveData<List<UserAccount>?>()
    private var _bscRecord = MutableLiveData<List<BaseSupportCategory>?>()

    // 信息录入信息
    var detailInfoRecord: LiveData<List<DetailInfo>?> = _detailInfoRecord

    // 购买的成品信息
    var soLineRecord: LiveData<List<SalesOrderLine>?> = _soLineRecord

    // 委派人员
    var appointUserRecord: LiveData<List<UserAccount>?> = _appointUserRecord

    // 服务类别
    var bscRecord: LiveData<List<BaseSupportCategory>?> = _bscRecord

    // 提示信息
    var remarkText = MutableLiveData<String>()

    // 返回值
    var reData = MutableLiveData<SysSqlReturn>()

    // 获取订单信息
    fun getSalesOrderInfo(soId: Int) {
        val url = urlBase + "SalesOrder/GetModel/${soId}"

        StringRequest(
            Request.Method.GET,
            url,
            {
                if (it.isNotEmpty() && it != "null") {
                    val gson =
                        GsonBuilder().registerTypeAdapter(Date::class.java, DateDeserializer())
                            .create()

                    gson.fromJson(it, SalesOrder::class.java)?.run {
                        val dlList = mutableListOf<DetailInfo>()

                        dlList.add(
                            DetailInfo(
                                titleString = "单号",
                                contentString = this.soId.toString()
                            )
                        )
                        dlList.add(DetailInfo("客户姓名", this.ciName))
                        dlList.add(DetailInfo("联系电话", this.ciTel))
                        dlList.add(DetailInfo("备用电话1", this.ciTel2))
                        dlList.add(DetailInfo("备用电话2", this.ciTel3))
                        dlList.add(DetailInfo("省", this.bGpName))
                        dlList.add(DetailInfo("市", this.bGcName))
                        dlList.add(DetailInfo("区", this.bGdName))
                        dlList.add(DetailInfo("详细地址", this.ciAddress))
                        dlList.add(DetailInfo("备注", this.soRemark))

                        _detailInfoRecord.value = dlList
                    }
                } else {
                    remarkText.value = "未找到客户信息"
                }
            },
            {
                remarkText.value =
                    getApplication<Application>().resources.getString(R.string.app_error)
//                Log.d("Ferroli Log", it.toString())
            }
        ).also {
            VolleySingleton.getInstance(getApplication()).requestQueue.add(it)
        }
    }

    // 获取成品列表
    fun getSalesOrderLineList(soId: Int) {
        val url = urlBase + "SalesOrder/Line_GetModelList/${soId}"

        StringRequest(
            Request.Method.GET,
            url,
            {
                if (it.isNotEmpty() && it != "null") {
                    val gson =
                        GsonBuilder().registerTypeAdapter(Date::class.java, DateDeserializer())
                            .create()

                    _soLineRecord.value =
                        gson.fromJson(it, Array<SalesOrderLine>::class.java).toList()
                } else {
                    remarkText.value = "未找到成品信息"
                }
            },
            {
                remarkText.value =
                    getApplication<Application>().resources.getString(R.string.app_error)
//                Log.d("Ferroli Log", it.toString())
            }
        ).also {
            VolleySingleton.getInstance(getApplication()).requestQueue.add(it)
        }
    }

    // 获取委派人员列表
    fun getAppointUser() {
        val empId = LoginInfo.getLoginEmpId(getApplication())
        val url = urlBase + "UserAccount/GetAppointUser/${empId}"

        StringRequest(
            Request.Method.GET,
            url,
            {
                if (it.isNotEmpty() && it != "null") {
                    val gson =
                        GsonBuilder().registerTypeAdapter(Date::class.java, DateDeserializer())
                            .create()

                    _appointUserRecord.value =
                        gson.fromJson(it, Array<UserAccount>::class.java).toList()
                } else {
                    remarkText.value = "未找到成品信息"
                }
            },
            {
                remarkText.value =
                    getApplication<Application>().resources.getString(R.string.app_error)
//                Log.d("Ferroli Log", it.toString())
            }
        ).also {
            VolleySingleton.getInstance(getApplication()).requestQueue.add(it)
        }
    }

    // 获取委派类型
    fun getBaseSupportCategory() {
        val url = urlBase + "Base/BaseSupportCategory_GetModelList/1=1"

        StringRequest(
            Request.Method.GET,
            url,
            {
                if (it.isNotEmpty() && it != "null") {
                    val gson =
                        GsonBuilder().registerTypeAdapter(Date::class.java, DateDeserializer())
                            .create()

                    _bscRecord.value =
                        gson.fromJson(it, Array<BaseSupportCategory>::class.java).toList()
                } else {
                    remarkText.value = "未找到委派信息"
                }
            },
            {
                remarkText.value =
                    getApplication<Application>().resources.getString(R.string.app_error)
//                Log.d("Ferroli Log", it.toString())
            }
        ).also {
            VolleySingleton.getInstance(getApplication()).requestQueue.add(it)
        }
    }

    fun saveData(bscPosition: Int, toEmpPosition: Int, soId: Int) {
        val formatter = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

        val url = urlBase + "SalesAppoint/Add"

        val empId = LoginInfo.getLoginEmpId(getApplication())

        // 转换一下，从位置拿到位置的ID
        val bscId = bscRecord.value?.get(bscPosition)?.bScId
        val toEmpId = appointUserRecord.value?.get(toEmpPosition)?.empId
        val dtNow = Date()

        val jsonObject = JSONObject()
        jsonObject.put("SOId", soId)
        jsonObject.put("BScId", bscId)
        jsonObject.put("SAEmpId", empId)
        jsonObject.put("SADate", formatter.format(dtNow))
        jsonObject.put("SAIsAgainAppoint", false)
        jsonObject.put("SAIsComplate", false)
        jsonObject.put("SAComplateDate", formatter.format(dtNow))
        jsonObject.put("SARemark", "")
        jsonObject.put("SAIsFinish", false)
        jsonObject.put("SFId", -1)

        val line = JSONObject()
        line.put("SAId", -1)
        line.put("SAlId", 1)
        line.put("SAlAppointDate", formatter.format(dtNow))
        line.put("SAlFromEmpId", empId)
        line.put("SAlToEmpId", toEmpId)
        line.put("SAlDate", formatter.format(dtNow))
        line.put("SAlIsTakeOrder", false)
        line.put("SAlTakeOrderDate", formatter.format(dtNow))
        line.put("SAlIsCurrent", true)
        line.put("SAlIsError", false)
        line.put("SAlRemark", "")

        jsonObject.put("Line", line)

        object : JsonObjectRequest(
            Method.POST,
            url,
            jsonObject,
            Response.Listener {
                if (it != null) {
                    reData.value = SysSqlReturn(it.getString("fOK"), it.getString("fMsg"))
                } else {
                    remarkText.value =
                        getApplication<Application>().resources.getString(R.string.app_error)
                }
            },
            Response.ErrorListener {
                remarkText.value =
                    getApplication<Application>().resources.getString(R.string.app_error)
//                Log.d("Ferroli Log", it.toString())
            }
        ) {
            override fun getBodyContentType(): String {
                return "application/json"
            }
        }.also {
            VolleySingleton.getInstance(getApplication()).requestQueue.add(it)
        }
    }
}