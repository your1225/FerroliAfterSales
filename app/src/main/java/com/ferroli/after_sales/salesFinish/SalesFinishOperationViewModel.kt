package com.ferroli.after_sales.salesFinish

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
import java.text.SimpleDateFormat
import java.util.*

class SalesFinishOperationViewModel(application: Application) : AndroidViewModel(application) {

    private var _salesFinishLineRecord = MutableLiveData<List<SalesFinishLine>?>()
    private var _baseProductInfoRecord = MutableLiveData<BaseProductInfo?>()
    private var _baseServiceTypeRecord = MutableLiveData<List<BaseServiceType>?>()

    // 已选委派
    private lateinit var selectedSaLine: SalesAppointLine

    // 所选配件
    var salesFinishLineRecord: LiveData<List<SalesFinishLine>?> = _salesFinishLineRecord

    // 所选产品
    var baseProductInfoRecord: LiveData<BaseProductInfo?> = _baseProductInfoRecord

    // 售后类别
    var baseServiceTypeRecord: LiveData<List<BaseServiceType>?> = _baseServiceTypeRecord

    // 二维码是否通过
    var qrCodePassed = MutableLiveData<Boolean>()

    // 完成日期
    var sfFinishDate = MutableLiveData<Date>()

    // 提示信息
    var remarkText = MutableLiveData<String>()

    // 返回值
    var reData = MutableLiveData<SysSqlReturn>()

    fun addBasePartInfo(item: BasePartInfo, buyCount: Int) {
        val line = SalesFinishLine(
            sfId = -1,
            sFlId = -1,
            bPcId = item.bPcId,
            bPaiCode = item.bPaiCode,
            sFlBarcode = "",
            sfDate = Date(),
            userName = "",
            sFlCount = buyCount,
            sFlAgentPrice = item.bPaiAgentPrice,
            sFlPrice = item.bPaiPrice,
            bPaiName = item.bPaiName,
            bPcName = item.bPcName,
            agentTotalMoney = -1f,
            sfEmpId = -1
        )

        _salesFinishLineRecord.value = _salesFinishLineRecord.value?.plus(line) ?: listOf(line)
    }

    fun addProductInfo(bPiCode: String) {
        val url = urlBase + "ProduceInfo/GetModelByCode/${bPiCode}"

        StringRequest(
            Request.Method.GET,
            url,
            {
                if (it.isNotEmpty() && it != "null") {
                    val gson =
                        GsonBuilder().registerTypeAdapter(Date::class.java, DateDeserializer())
                            .create()

                    _baseProductInfoRecord.value = gson.fromJson(it, BaseProductInfo::class.java)
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

    fun getBaseServiceType() {
        val url = urlBase + "Base/BaseServiceType_GetModelList/1=1"

        StringRequest(
            Request.Method.GET,
            url,
            {
                if (it.isNotEmpty() && it != "null") {
                    val gson =
                        GsonBuilder().registerTypeAdapter(Date::class.java, DateDeserializer())
                            .create()

                    _baseServiceTypeRecord.value =
                        gson.fromJson(it, Array<BaseServiceType>::class.java).toList()
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

    fun removeBasePartInfo(item: SalesFinishLine) {
        _salesFinishLineRecord.value = _salesFinishLineRecord.value?.minus(item) ?: listOf()
    }

    fun getSalesAppointLineCurrent(saId: Int) {
        val url = urlBase + "SalesAppoint/Line_GetCurrentModel/${saId}"

        StringRequest(
            Request.Method.GET,
            url,
            {
                if (it.isNotEmpty() && it != "null") {
                    val gson =
                        GsonBuilder().registerTypeAdapter(Date::class.java, DateDeserializer())
                            .create()

                    selectedSaLine = gson.fromJson(it, SalesAppointLine::class.java)
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

    fun getBaseProductInfoByQrCode(qrCode: String) {
        val url = urlBase + "ProduceInfo/GetModelByQrCode/${qrCode}"

        StringRequest(
            Request.Method.GET,
            url,
            {
                if (it.isNotEmpty() && it != "null") {
                    val gson =
                        GsonBuilder().registerTypeAdapter(Date::class.java, DateDeserializer())
                            .create()

                    _baseProductInfoRecord.value = gson.fromJson(it, BaseProductInfo::class.java)

                    qrCodePassed.value = true
                } else {
                    remarkText.value = "无法通过二维码获取产品信息"
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
        pcBarcode: String,
        bStPosition: Int,
        sfUnderWarranty: Boolean,
        sfRemark: String,
        sfNeedApprove: Boolean
    ) {
        val url = urlBase + "SalesFinish/Add"

        val formatter = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        val dtNow = Date()

        val empId = LoginInfo.getLoginEmpId(getApplication())
//        val empId = 1225

        val array = JSONArray()

        salesFinishLineRecord.value?.run {
            for (item in this) {
                val obj = JSONObject()
                obj.put("SFId", item.sfId)
                obj.put("SFlId", item.sFlId)
                obj.put("BPcId", item.bPcId)
                obj.put("SFlBarcode", item.sFlBarcode)
                obj.put("BPaiCode", item.bPaiCode)
                obj.put("SFlCount", item.sFlCount)
                obj.put("SFlAgentPrice", item.sFlAgentPrice)
                obj.put("SFlPrice", item.sFlPrice)

                array.put(obj)
            }
        }

        // 转换一下，从位置拿到位置的ID
        val bStId: Int = if (bStPosition == -1) {
            -1
        } else {
            baseServiceTypeRecord.value?.get(bStPosition)?.bStId ?: -1
        }

        val jsonObject = JSONObject()
//        jsonObject.put("SFId", -1)
        jsonObject.put("PCBarcode", pcBarcode)
        jsonObject.put("BPiCode", baseProductInfoRecord.value?.bPiCode)
        jsonObject.put("BStId", bStId)
        jsonObject.put("SFUnderWarranty", sfUnderWarranty)
        jsonObject.put("SFFinishDate", sfFinishDate.value?.let { formatter.format(it) } ?: formatter.format(dtNow))
        jsonObject.put("SOId", selectedSaLine.soId)
        jsonObject.put("SAId", selectedSaLine.saId)
        jsonObject.put("SFEmpId", empId)
        jsonObject.put("SFDate", formatter.format(dtNow))
        jsonObject.put("SFRemark", sfRemark)
        jsonObject.put("SFNeedApprove", sfNeedApprove)
//        jsonObject.put("SFIsApprove", false)
        jsonObject.put("SFApproveDate", formatter.format(dtNow))
//        jsonObject.put("SFApproveEmpId", -1)
//        jsonObject.put("SFIsRevisit", false)
//        jsonObject.put("SRId", -1)
//        jsonObject.put("SFIsExamine", false)
//        jsonObject.put("SFIsLineExamine", false)
//        jsonObject.put("SEId", -1)

        jsonObject.put("Lines", array)

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

    fun clearData() {
        qrCodePassed.value = false
        _baseProductInfoRecord.postValue(null)
        _salesFinishLineRecord.postValue(null)
    }
}