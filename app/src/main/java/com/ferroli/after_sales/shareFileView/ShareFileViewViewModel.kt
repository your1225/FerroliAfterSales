package com.ferroli.after_sales.shareFileView

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.ferroli.after_sales.R
import com.ferroli.after_sales.entity.CusServiceFile
import com.ferroli.after_sales.entity.VolleySingleton
import com.ferroli.after_sales.entity.urlBase
import com.ferroli.after_sales.utils.DateDeserializer
import com.google.gson.GsonBuilder
import java.util.*

class ShareFileViewViewModel(application: Application) : AndroidViewModel(application) {

    var shareFileRecord = MutableLiveData<CusServiceFile?>()

    var remarkText = MutableLiveData<String>()

    fun refreshFile() {
        val url = urlBase + "File/GetShareFiles"

        StringRequest(
            Request.Method.GET,
            url,
            {
                if (it.isNotEmpty() && it != "null") {
                    val gson =
                        GsonBuilder().registerTypeAdapter(Date::class.java, DateDeserializer())
                            .create()

                    shareFileRecord.value = gson.fromJson(it, CusServiceFile::class.java)
                } else {
                    remarkText.value = "未发现任何文件"
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