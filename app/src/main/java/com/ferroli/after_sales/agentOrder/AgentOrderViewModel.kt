package com.ferroli.after_sales.agentOrder

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
import java.util.*

class AgentOrderViewModel(application: Application) : AndroidViewModel(application) {

    private var _basePartInfoRecord = MutableLiveData<List<AgentOrderLine>?>()

    // 选择的物品
    var basePartInfoRecord: LiveData<List<AgentOrderLine>?> = _basePartInfoRecord

    // 收件人
    val aoReceiveName = MutableLiveData<String>()

    // 电话
    val aoReceiveTel = MutableLiveData<String>()

    // 送货地址
    val aoReceiveAddress = MutableLiveData<String>()

    // 备注
    val aoRemark = MutableLiveData<String>()

    // 提示信息
    var remarkText = MutableLiveData<String>()

    // 返回值
    var reData = MutableLiveData<SysSqlReturn>()

    fun getLastInfo() {
        val empId = LoginInfo.getLoginEmpId(getApplication())

        if (empId == 0) {
            return
        }

        val url = urlBase + "AgentOrder/GetLastData/${empId}"

        StringRequest(
            Request.Method.GET,
            url,
            {
                if (it.isNotEmpty() && it != "null") {
                    val gson =
                        GsonBuilder().registerTypeAdapter(Date::class.java, DateDeserializer())
                            .create()

                    val ao = gson.fromJson(it, AgentOrder::class.java)
                    aoReceiveName.value = ao.aoReceiveName
                    aoReceiveTel.value = ao.aoReceiveTel
                    aoReceiveAddress.value = ao.aoReceiveAddress
                } else {
                    remarkText.value = "未找到历史订购信息"
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

    fun addBasePartInfo(item: BasePartInfo, buyCount: Int) {
        val line = AgentOrderLine(
            aoId = -1,
            aOlId = -1,
            bPaiCode = item.bPaiCode,
            aOlCount = buyCount,
            aOlAgentPrice = item.bPaiAgentPrice,
            aOlPrice = item.bPaiPrice,
            bPcId = item.bPcId,
            bPaiName = item.bPaiName,
            bPcName = item.bPcName,
            aoReceiveEmpId = LoginInfo.getLoginEmpId(getApplication()),
            aoIsApprove = false,
            aoIsSend = false,
            aOlApproveCount = 0,
            aOlApproveRemark = "",
            stockCount = 0
        )

        _basePartInfoRecord.value = _basePartInfoRecord.value?.plus(line) ?: listOf(line)
    }

    fun removeBasePartInfo(item: AgentOrderLine) {
        _basePartInfoRecord.value = _basePartInfoRecord.value?.minus(item) ?: listOf()
    }

    fun saveData(receiveName: String, receiveTel: String, receiveAddress: String, remark: String) {
        val url = urlBase + "AgentOrder/Post"

        val empId = LoginInfo.getLoginEmpId(getApplication())
//        val empId = 1225

        val array = JSONArray()

        basePartInfoRecord.value?.run {
            for (item in this) {
                val obj = JSONObject()
                obj.put("AOId", item.aoId)
                obj.put("AOlId", item.aOlId)
                obj.put("BPaiCode", item.bPaiCode)
                obj.put("AOlCount", item.aOlCount)
                obj.put("AOlAgentPrice", item.aOlAgentPrice)
                obj.put("AOlPrice", item.aOlPrice)
                obj.put("AOlApproveCount", item.aOlApproveCount)
                obj.put("AOlApproveRemark", item.aOlApproveRemark)

                array.put(obj)
            }
        }

        val jsonObject = JSONObject()
        jsonObject.put("AOReceiveEmpId", empId)
        jsonObject.put("AOReceiveName", receiveName)
        jsonObject.put("AOReceiveTel", receiveTel)
        jsonObject.put("AOReceiveAddress", receiveAddress)
        jsonObject.put("AOEmpId", empId)
        jsonObject.put("AORemark", remark)

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
        _basePartInfoRecord.postValue(null)

        this.reData.postValue(null)
    }
}