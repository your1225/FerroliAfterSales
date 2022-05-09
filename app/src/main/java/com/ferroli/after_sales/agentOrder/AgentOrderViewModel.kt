package com.ferroli.after_sales.agentOrder

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.ferroli.after_sales.R
import com.ferroli.after_sales.entity.AgentOrder
import com.ferroli.after_sales.entity.BasePartInfo
import com.ferroli.after_sales.entity.VolleySingleton
import com.ferroli.after_sales.entity.urlBase
import com.ferroli.after_sales.utils.DateDeserializer
import com.google.gson.GsonBuilder
import java.util.*

class AgentOrderViewModel(application: Application) : AndroidViewModel(application) {

    private var _basePartInfoRecord = MutableLiveData<List<BasePartInfo>>()

    // 选择的物品
    var basePartInfoRecord: LiveData<List<BasePartInfo>> = _basePartInfoRecord

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

    fun getLastInfo(userId: Int) {
        if (userId == 0) {
            return
        }

        val url = urlBase + "AgentOrder/GetLastData/${userId}"

        val stringRequest = StringRequest(
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
        )

        VolleySingleton.getInstance(getApplication()).requestQueue.add(stringRequest)
    }
}