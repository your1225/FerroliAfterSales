package com.ferroli.after_sales.utils

import android.util.Log
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import java.lang.Exception
import java.lang.reflect.Type
import java.text.SimpleDateFormat
import java.util.*

class DateDeserializer : JsonDeserializer<Date> {
    override fun deserialize(
        json: JsonElement?,
        typeOfT: Type?,
        context: JsonDeserializationContext?
    ): Date {
        var date = Date()

        if (json != null) {
            val dateString = json.asString
            val format =
                SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.CHINESE)
            format.timeZone = TimeZone.getTimeZone("GMT+:08:00")

            try {
                date = format.parse(dateString)!!

                val cal = Calendar.getInstance()
                cal.time = date
                cal.add(Calendar.HOUR, -8)
                date = cal.time
            } catch (e: Exception) {
                Log.e("Ferroli Error", e.toString())
            }
        }

        return date
    }

}