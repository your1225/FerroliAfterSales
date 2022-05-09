package com.ferroli.after_sales.utils

import android.content.Context
import android.content.pm.PackageManager

object AppUtils {
    /**
     * 获取当前应用版本名
     *
     * @param context
     * @return
     */
    fun getCurrentVersionName(context: Context): String? {
        val packageManager = context.packageManager
        try {
            val packageInfo = packageManager.getPackageInfo(context.packageName, 0)
            return packageInfo.versionName
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }
        return null
    }
}