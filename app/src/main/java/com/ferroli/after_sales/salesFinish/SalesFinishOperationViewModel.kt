package com.ferroli.after_sales.salesFinish

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.ferroli.after_sales.R
import com.ferroli.after_sales.entity.*
import com.ferroli.after_sales.utils.DateDeserializer
import com.ferroli.after_sales.utils.LoginInfo
import com.google.gson.GsonBuilder
import java.util.*

class SalesFinishOperationViewModel(application: Application) : AndroidViewModel(application) {

    private var _salesFinishLineRecord = MutableLiveData<List<SalesFinishLine>?>()
    private var _baseProductInfoRecord = MutableLiveData<BaseProductInfo?>()
    private var _baseServiceTypeRecord = MutableLiveData<List<BaseServiceType>?>()

    // 所选配件
    var salesFinishLineRecord: LiveData<List<SalesFinishLine>?> = _salesFinishLineRecord

    // 所选产品
    var baseProductInfoRecord: LiveData<BaseProductInfo?> = _baseProductInfoRecord

    // 售后类别
    var baseServiceTypeRecord: LiveData<List<BaseServiceType>?> = _baseServiceTypeRecord

    // 提示信息
    var remarkText = MutableLiveData<String>()

    // 返回值
    var reData = MutableLiveData<SysSqlReturn>()

    fun addBasePartInfo(item: BasePartInfo, buyCount: Int) {
        val line = SalesFinishLine(
            sfId = -1,
            sFlId= -1,
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

                    val pi = gson.fromJson(it, BaseProductInfo::class.java)

                    _baseProductInfoRecord.value = pi
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
        val url = urlBase + "Base/BaseServiceType_GetModelList/1=1}"

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

    fun clearData() {
        _baseProductInfoRecord.postValue(null)
        _salesFinishLineRecord.postValue(null)
    }
}