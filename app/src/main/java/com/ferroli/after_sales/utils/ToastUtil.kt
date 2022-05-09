package com.ferroli.after_sales.utils

import android.content.Context
import android.media.Ringtone
import android.media.RingtoneManager
import android.net.Uri
import android.widget.Toast
import androidx.annotation.StringRes

object ToastUtil {
    fun showToast(context: Context, @StringRes resId: Int) {
        Toast.makeText(context, resId, Toast.LENGTH_LONG).show()

        this.alarmSound(context)
    }

    fun showToast(context: Context, resString: String) {
        Toast.makeText(context, resString, Toast.LENGTH_LONG).show()

        this.alarmSound(context)
    }

    private fun alarmSound(context: Context) {
        val uri: Uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val ringtone: Ringtone = RingtoneManager.getRingtone(context, uri)
        ringtone.play()
    }
}