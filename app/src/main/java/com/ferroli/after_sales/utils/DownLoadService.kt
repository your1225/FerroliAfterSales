package com.ferroli.after_sales.utils

import android.app.DownloadManager
import android.app.Service
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.Uri
import android.os.Environment
import android.os.IBinder
import android.util.Log
import java.io.File

class DownLoadService: Service() {
    private var downloadManager: DownloadManager? = null
    private var downLoadBroadCastReceiver: DownLoadBroadCastReceiver? = null

    private var mReqId: Long = 0

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        // fix bug : 装不了新版本，在下载之前应该删除已有文件
        val apkFile = File(
            applicationContext.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS),
            "ferroli_after_sales_update.apk"
        )

        if (apkFile.exists()) {
            apkFile.delete()
        }

        val url = intent!!.getStringExtra("url")

        //实例化DownloadManager下载对象
        downloadManager = getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager

        //注册下载完成广播
        downLoadBroadCastReceiver = DownLoadBroadCastReceiver()
        val intentFilter = IntentFilter()
        intentFilter.addAction(DownloadManager.ACTION_DOWNLOAD_COMPLETE)
        registerReceiver(downLoadBroadCastReceiver, intentFilter)

        val request = DownloadManager.Request(Uri.parse(url))
        //设置title
        request.setTitle("ferroli_after_sales_update")
        // 设置描述
        request.setDescription("ferroli update")
        // 完成后显示通知栏
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
        request.setDestinationInExternalFilesDir(
            applicationContext,
            Environment.DIRECTORY_DOWNLOADS,
            "ferroli_after_sales_update.apk"
        )
        //在手机SD卡上创建一个download文件夹
        // Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).mkdir() ;
        //指定下载到SD卡的/download/my/目录下
        // request.setDestinationInExternalPublicDir("/codoon/","test.apk");

        request.setMimeType("application/vnd.android.package-archive")
        //记住reqId
        mReqId = downloadManager!!.enqueue(request)

        return START_STICKY
    }

    inner class DownLoadBroadCastReceiver : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            val completeDownLoadId = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1)
            if (mReqId == completeDownLoadId) {
                //下载完成
                unregisterReceiver(downLoadBroadCastReceiver)

//                installApk()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (downLoadBroadCastReceiver != null) {
            unregisterReceiver(downLoadBroadCastReceiver)
        }
        Log.d("DownLoadService", "-----------------DownLoadService  onDestory")
    }

}