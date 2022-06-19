package com.ferroli.after_sales.salesAppointLine

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

class SalesAppointLineViewModel(application: Application) : AndroidViewModel(application) {
    private var _salesAppointLineRecord = MutableLiveData<List<SalesAppointLine>?>()

    var salesAppointLineRecord: LiveData<List<SalesAppointLine>?> = _salesAppointLineRecord

    // 提示信息
    var remarkText = MutableLiveData<String>()

    fun getSalesAppointToMeList(telStr: String) {
        val empId = LoginInfo.getLoginEmpId(getApplication())

        var tel = telStr
        if (tel.isEmpty()) {
            tel = "no"
        }

        val url = urlBase + "SalesAppoint/Line_GetModelListToMe/${empId}/${tel}"

        StringRequest(
            Request.Method.GET,
            url,
            {
                if (it.isNotEmpty() && it != "null") {
                    val gson =
                        GsonBuilder().registerTypeAdapter(Date::class.java, DateDeserializer())
                            .create()

                    val list = gson.fromJson(it, Array<SalesAppointLine>::class.java).toList()

                    _salesAppointLineRecord.value = list
                } else {
                    remarkText.value = "未找到未委派单据"
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