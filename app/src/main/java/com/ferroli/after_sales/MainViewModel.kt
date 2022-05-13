package com.ferroli.after_sales

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.ferroli.after_sales.entity.VersionInfo
import com.ferroli.after_sales.entity.VolleySingleton
import com.ferroli.after_sales.entity.urlBase
import com.google.gson.Gson

class MainViewModel(application: Application, private var hanlde: SavedStateHandle) :
    AndroidViewModel(application) {

    // 版本信息
    var versionInfo = MutableLiveData<VersionInfo>()

    fun getLastVersionData() {
        val url = urlBase + "VersionInfo/GetLastData/AfterSalesApp"

        StringRequest(
            Request.Method.GET,
            url,
            {
                versionInfo.value = Gson().fromJson(it, VersionInfo::class.java)
            },
            {
                Log.d("Ferroli Log", it.toString())
            }
        ).also {
            VolleySingleton.getInstance(getApplication()).requestQueue.add(it)
        }
    }
}