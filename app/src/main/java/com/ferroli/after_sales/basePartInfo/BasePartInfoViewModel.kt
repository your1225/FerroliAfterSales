package com.ferroli.after_sales.basePartInfo

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.ferroli.after_sales.R
import com.ferroli.after_sales.entity.BasePartInfo
import com.ferroli.after_sales.entity.VolleySingleton
import com.ferroli.after_sales.entity.urlBase
import com.ferroli.after_sales.utils.DateDeserializer
import com.google.gson.GsonBuilder
import java.util.*

class BasePartInfoViewModel(application: Application) : AndroidViewModel(application) {
    // 物品列表
    private var _basePartInfoRecord = MutableLiveData<List<BasePartInfo>?>()
    var basePartInfoRecord: LiveData<List<BasePartInfo>?> = _basePartInfoRecord

    // 已选物品
    var selectedItem = MutableLiveData<BasePartInfo>()

    // 提示信息
    var remarkText = MutableLiveData<String>()

    // 获取搜索列表
    fun getSearchData(searchString: String) {
        val url = urlBase + "Base/BasePartInfo_GetModelList/${searchString}"

        val stringRequest = StringRequest(
            Request.Method.GET,
            url,
            {
                if (it.isNotEmpty() && it != "null") {
                    val gson =
                        GsonBuilder().registerTypeAdapter(Date::class.java, DateDeserializer())
                            .create()

                    _basePartInfoRecord.value =
                        gson.fromJson(it, Array<BasePartInfo>::class.java).toList()
                } else {
                    remarkText.value = "未找到配件信息"
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