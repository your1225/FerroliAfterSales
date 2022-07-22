package com.ferroli.after_sales.utils

import android.content.Context
import android.content.SharedPreferences
import com.ferroli.after_sales.R

object LoginInfo {
    fun getLoginEmpId(context: Context): Int {
        var empId = 0

        val shpName = context.resources.getString(R.string.shp_name)
        val keyEmpId: String =
            context.resources.getString(R.string.shp_emp_id)

        val shp: SharedPreferences? = context.getSharedPreferences(shpName, Context.MODE_PRIVATE)
        if (shp != null)
            empId = shp.getInt(keyEmpId, 0)

        return empId
    }

    fun getLoginEmpName(context: Context): String {
        var empName = ""

        val shpName = context.resources.getString(R.string.shp_name)
        val keyEmpName: String =
            context.resources.getString(R.string.shp_emp_name)

        val shp: SharedPreferences? = context.getSharedPreferences(shpName, Context.MODE_PRIVATE)
        if (shp != null)
            empName = shp.getString(keyEmpName, "").toString()

        return empName
    }

    fun getLoginEmpAppointLevel(context: Context): Int {
        var empAppointLevel = 0

        val shpName = context.resources.getString(R.string.shp_name)
        val keyEmpAppointLevel: String =
            context.resources.getString(R.string.shp_emp_appoint_level)

        val shp: SharedPreferences? = context.getSharedPreferences(shpName, Context.MODE_PRIVATE)
        if (shp != null)
            empAppointLevel = shp.getInt(keyEmpAppointLevel, 0)

        return empAppointLevel
    }
}