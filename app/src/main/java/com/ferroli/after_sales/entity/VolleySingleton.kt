package com.ferroli.after_sales.entity

import android.content.Context
import com.android.volley.RequestQueue
import com.android.volley.toolbox.Volley

//const val urlBase: String = "https://barcode.ferroli.com.cn:9013/api/"
// 测试的服务
const val urlBase: String = "https://barcode.ferroli.com.cn:9014/api/"
//const val urlBase: String = "https://app.ultra-union.com/api/"
const val urlFileBase: String = "http://183.236.245.250:9011/MaterialImg/"
//const val urlDownload: String = "http://183.236.245.250:9011/wr"

class VolleySingleton private constructor(context: Context) {
    companion object {
        private var INSTANCE: VolleySingleton? = null
        fun getInstance(context: Context) =
            INSTANCE ?: synchronized(this) {
                VolleySingleton(context).also {
                    INSTANCE = it
                }
            }
    }

    val requestQueue: RequestQueue by lazy {
        Volley.newRequestQueue(context.applicationContext)
    }
}