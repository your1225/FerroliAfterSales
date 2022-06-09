package com.ferroli.after_sales.baseProductInfo

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.ferroli.after_sales.R
import com.ferroli.after_sales.entity.BaseProductInfo
import com.ferroli.after_sales.entity.VolleySingleton
import com.ferroli.after_sales.entity.urlBase
import com.ferroli.after_sales.utils.DateDeserializer
import com.google.gson.GsonBuilder
import java.util.*

class BaseProductInfoViewModel(application: Application) : AndroidViewModel(application) {
    // 物品列表
    private var _baseProductInfoRecord = MutableLiveData<List<BaseProductInfo>?>()
    var baseProductInfoRecord: LiveData<List<BaseProductInfo>?> = _baseProductInfoRecord

    // 已选物品
//    var selectedItem = MutableLiveData<BaseProductInfo>()

    // 提示信息
    var remarkText = MutableLiveData<String>()

    // 获取搜索列表
    fun getSearchData(searchString: String) {
        val url = urlBase + "ProduceInfo/GetModelList/${searchString.replace(".", "dot").replace("+", "add")}"

        StringRequest(
            Request.Method.GET,
            url,
            {
                if (it.isNotEmpty() && it != "null") {
                    val gson =
                        GsonBuilder().registerTypeAdapter(Date::class.java, DateDeserializer())
                            .create()

                    _baseProductInfoRecord.value =
                        gson.fromJson(it, Array<BaseProductInfo>::class.java).toList()
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

}