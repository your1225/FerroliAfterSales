package com.ferroli.after_sales.utils

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.activity.result.contract.ActivityResultContract
import com.king.zxing.CaptureActivity

class CameraActivityResultContract : ActivityResultContract<String, String>() {
    private val KEY_TITLE = "key_title"
    private val KEY_IS_CONTINUOUS = "key_continuous_scan"
    val REQUEST_CODE_SCAN = 0X01

    override fun createIntent(context: Context, input: String?): Intent {
        return Intent(context, CaptureActivity::class.java).apply {
            putExtra(KEY_TITLE, input)
            putExtra(KEY_IS_CONTINUOUS, false)
        }
    }

    override fun parseResult(resultCode: Int, intent: Intent?): String {
        val data = intent?.getStringExtra("SCAN_RESULT")
        return if (resultCode == Activity.RESULT_OK && data != null) data
        else "no data"
    }
}