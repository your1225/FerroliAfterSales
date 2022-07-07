package com.ferroli.after_sales.secMain

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

class SecMainViewModel(application: Application) : AndroidViewModel(application) {

    private var _dingUserWithState = MutableLiveData<DingUserWithState?>()

    // 用户信息及状态
    var dingUserWithState: LiveData<DingUserWithState?> = _dingUserWithState

    // 提示信息
    var remarkText = MutableLiveData<String>()

    fun getUserInfo() {
        val empId = LoginInfo.getLoginEmpId(getApplication())

        if (empId == 0) {
            return
        }

        val url = urlBase + "DingUser/GetDingUserInfo/${empId}"

        StringRequest(
            Request.Method.GET,
            url,
            {
                if (it.isNotEmpty() && it != "null") {
                    val gson =
                        GsonBuilder().registerTypeAdapter(Date::class.java, DateDeserializer())
                            .create()

                    _dingUserWithState.value = gson.fromJson(it, DingUserWithState::class.java)
                } else {
                    remarkText.value = "未找到用户信息"
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