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
import com.ferroli.after_sales.entity.VolleySingleton
import com.ferroli.after_sales.entity.urlBase

class LoginViewModel(application: Application, private var handle: SavedStateHandle) :
    AndroidViewModel(application) {
    private val keyEmpId: String =
        application.resources.getString(R.string.shp_emp_id)
    private val keyEmpName: String =
        application.resources.getString(R.string.shp_emp_name)
    private val shpName =
        application.resources.getString(R.string.shp_name)

    var reString = MutableLiveData<String>()

    private var _empId = MutableLiveData<Int>().also {
        if (!handle.contains(keyEmpId)) {
            handle.set(keyEmpId, 0)
        }
        it.value = handle.get(keyEmpId)
    }

    private var _empName = MutableLiveData<String>().also {
        if (!handle.contains(keyEmpName)) {
            handle.set(keyEmpName, "")
        }
        it.value = handle.get(keyEmpName)
    }

    // 员工工号
//    val empId: LiveData<Int> = _empId
    // 员工姓名
    val empName: LiveData<String> = _empName

    fun saveUserInfo(empId: Int, empName: String) {
        val shp: SharedPreferences =
            getApplication<Application>().getSharedPreferences(
                shpName,
                Context.MODE_PRIVATE
            )
        val editor: SharedPreferences.Editor = shp.edit()
        editor.putInt(keyEmpId, empId)
        editor.putString(keyEmpName, empName)
        editor.apply()
    }

    // 刷新/获取用户信息
    fun refreshUserInfo() {
        val shp: SharedPreferences? =
            getApplication<Application>().getSharedPreferences(shpName, Context.MODE_PRIVATE)
        if (shp != null) {
            _empId.value = shp.getInt(keyEmpId, 0)
            _empName.value = shp.getString(keyEmpName, "")
        }
    }

    fun loginUser(userName: String, password: String){
        val url = urlBase + "UserAccount/LoginCheck/${userName}/${password}"

        StringRequest(
            Request.Method.GET,
            url,
            {
                reString.value = it
            },
            {
                Log.d("Ferroli Log", it.toString())
            }
        ).also {
            VolleySingleton.getInstance(getApplication()).requestQueue.add(it)
        }
    }
}