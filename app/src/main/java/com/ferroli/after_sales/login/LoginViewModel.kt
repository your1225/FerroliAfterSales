package com.ferroli.after_sales.login

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.ferroli.after_sales.R
import com.ferroli.after_sales.entity.UserAccount
import com.ferroli.after_sales.entity.VolleySingleton
import com.ferroli.after_sales.entity.urlBase
import com.ferroli.after_sales.utils.DateDeserializer
import com.google.gson.GsonBuilder
import java.util.*

class LoginViewModel(application: Application, private var handle: SavedStateHandle) :
    AndroidViewModel(application) {
    private val keyEmpId: String =
        application.resources.getString(R.string.shp_emp_id)
    private val keyEmpName: String =
        application.resources.getString(R.string.shp_emp_name)
    private val keyEmpAppointLevel: String =
        application.resources.getString(R.string.shp_emp_appoint_level)
    private val shpName: String =
        application.resources.getString(R.string.shp_name)

    var userAccount = MutableLiveData<UserAccount>()

    private var _empId = MutableLiveData<Int>().also {
        if (!handle.contains(keyEmpId)) {
            handle[keyEmpId] = 0
        }
        it.value = handle[keyEmpId]
    }

    private var _empName = MutableLiveData<String>().also {
        if (!handle.contains(keyEmpName)) {
            handle[keyEmpName] = ""
        }
        it.value = handle[keyEmpName]
    }
    private var _empAppointLevel = MutableLiveData<Int>().also {
        if (!handle.contains(keyEmpAppointLevel)) {
            handle[keyEmpAppointLevel] = 0
        }
        it.value = handle[keyEmpAppointLevel]
    }

    // 员工工号
    // val empId: LiveData<Int> = _empId
    // 员工姓名
    val empName: LiveData<String> = _empName

    fun saveUserInfo(ua: UserAccount) {
        val shp: SharedPreferences =
            getApplication<Application>().getSharedPreferences(
                shpName,
                Context.MODE_PRIVATE
            )
        val editor: SharedPreferences.Editor = shp.edit()
        editor.putInt(keyEmpId, ua.empId)
        editor.putString(keyEmpName, ua.userName)
        editor.putInt(keyEmpAppointLevel, ua.appointLevel)
        editor.apply()
    }

    // 刷新/获取用户信息
    fun refreshUserInfo() {
        val shp: SharedPreferences? =
            getApplication<Application>().getSharedPreferences(shpName, Context.MODE_PRIVATE)
        if (shp != null) {
            _empId.value = shp.getInt(keyEmpId, 0)
            _empName.value = shp.getString(keyEmpName, "")
            _empAppointLevel.value = shp.getInt(keyEmpAppointLevel, 0)
        }
    }

    fun loginUser(userName: String, password: String) {
        val url = urlBase + "UserAccount/LoginCheckNew/${userName.replace(".", "dot")}/${password}"

        StringRequest(
            Request.Method.GET,
            url,
            {
                if (it.isNotEmpty() && it != "null") {
                    val gson =
                        GsonBuilder().registerTypeAdapter(Date::class.java, DateDeserializer())
                            .create()

                    userAccount.value = gson.fromJson(it, UserAccount::class.java)
                }
            },
            {
                Log.d("Ferroli Log", it.toString())
            }
        ).also {
            VolleySingleton.getInstance(getApplication()).requestQueue.add(it)
        }
    }
}