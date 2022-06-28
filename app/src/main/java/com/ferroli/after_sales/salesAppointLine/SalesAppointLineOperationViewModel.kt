package com.ferroli.after_sales.salesAppointLine

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
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*

class SalesAppointLineOperationViewModel(application: Application) : AndroidViewModel(application) {
    private var _detailInfoRecord = MutableLiveData<List<DetailInfo>?>()
    private var _saLineRecord = MutableLiveData<List<SalesAppointLine>?>()
    private var _appointUserRecord = MutableLiveData<List<UserAccount>?>()
    private var _soLineRecord = MutableLiveData<List<SalesOrderLine>?>()

    // 已选委派
    private lateinit var selectedSa: SalesAppoint
    private lateinit var selectedSaLine: SalesAppointLine

    // 购买的成品信息
    var soLineRecord: LiveData<List<SalesOrderLine>?> = _soLineRecord

    // 委派信息
    var detailInfoRecord: LiveData<List<DetailInfo>?> = _detailInfoRecord

    // 委派行
    var saLineRecord: LiveData<List<SalesAppointLine>?> = _saLineRecord

    // 委派人员
    var appointUserRecord: LiveData<List<UserAccount>?> = _appointUserRecord

    // 提示信息
    var remarkText = MutableLiveData<String>()

    // 返回值
    var reData = MutableLiveData<SysSqlReturn>()

    // 获取订单信息
    fun getSalesAppointInfo(saId: Int) {
        val url = urlBase + "SalesAppoint/GetModel/${saId}"

        StringRequest(
            Request.Method.GET,
            url,
            {
                if (it.isNotEmpty() && it != "null") {
                    val gson =
                        GsonBuilder().registerTypeAdapter(Date::class.java, DateDeserializer())
                            .create()

                    selectedSa = gson.fromJson(it, SalesAppoint::class.java)

                    getSalesOrderLineList(selectedSa.soId)

                    val dlList = mutableListOf<DetailInfo>()

                    dlList.add(
                        DetailInfo(
                            titleString = "单号",
                            contentString = selectedSa.saId.toString()
                        )
                    )
                    dlList.add(DetailInfo("客户姓名", selectedSa.ciName))
                    dlList.add(DetailInfo("联系电话", selectedSa.ciTel))
                    dlList.add(DetailInfo("备用电话1", selectedSa.ciTel2))
                    dlList.add(DetailInfo("备用电话2", selectedSa.ciTel3))
                    dlList.add(DetailInfo("省", selectedSa.bGpName))
                    dlList.add(DetailInfo("市", selectedSa.bGcName))
                    dlList.add(DetailInfo("区", selectedSa.bGdName))
                    dlList.add(DetailInfo("服务类别", selectedSa.bScName))
                    dlList.add(DetailInfo("备注", selectedSa.saRemark))

                    _detailInfoRecord.value = dlList
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

    fun getSalesAppointLineList(saId: Int) {
        val url = urlBase + "SalesAppoint/Line_GetModelList/${saId}"

        StringRequest(
            Request.Method.GET,
            url,
            {
                if (it.isNotEmpty() && it != "null") {
                    val gson =
                        GsonBuilder().registerTypeAdapter(Date::class.java, DateDeserializer())
                            .create()

                    val lines = gson.fromJson(it, Array<SalesAppointLine>::class.java).toList()

                    _saLineRecord.value = lines

                    selectedSaLine = lines.single { saLine ->
                        saLine.sAlIsCurrent
                    }
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

    // 获取成品列表
    private fun getSalesOrderLineList(soId: Int) {
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

    fun saveData(
        toEmpPosition: Int,
        isTakeOrder: Boolean,
        isError: Boolean,
        isComplete: Boolean,
        remark: String
    ) {
        val formatter = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())

        val url = urlBase + "SalesAppoint/AddLine"

        val loginEmpId = LoginInfo.getLoginEmpId(getApplication())

        // 转换一下，从位置拿到位置的ID
        val selectedEmpId: Int = if (toEmpPosition == -1){
            -1
        } else {
            appointUserRecord.value?.get(toEmpPosition)?.empId ?: -1
        }

        val dtNow = Date()

        val fromEmpId: Int
        val toEmpId: Int

        if (!isTakeOrder && !isError && !isComplete) {
            // 普通派单 的时候
            fromEmpId = loginEmpId
            toEmpId = selectedEmpId
        } else if (isError) {
            // 返单 的时候
            fromEmpId = loginEmpId
            toEmpId = selectedSaLine.sAlFromEmpId
        } else {
            // 接单 取消 的时候
            fromEmpId = selectedSaLine.sAlFromEmpId
            toEmpId = selectedSaLine.sAlToEmpId
        }

        val jsonObject = JSONObject()
        jsonObject.put("SAId", selectedSaLine.saId)
        jsonObject.put("SAlId", -1)
        jsonObject.put("SAlAppointDate", formatter.format(dtNow))
        jsonObject.put("SAlFromEmpId", fromEmpId)
        jsonObject.put("SAlToEmpId", toEmpId)
        jsonObject.put("SAlDate", formatter.format(dtNow))
        jsonObject.put("SAlIsTakeOrder", isTakeOrder)
        jsonObject.put("SAlTakeOrderDate", formatter.format(dtNow))
        jsonObject.put("SAlIsCurrent", true)
        jsonObject.put("SAlIsError", isError)
        jsonObject.put("SAlRemark", remark)
        jsonObject.put("IsComplete", isComplete)
        jsonObject.put("SOId", selectedSaLine.soId)

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