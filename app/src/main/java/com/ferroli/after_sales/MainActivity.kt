package com.ferroli.after_sales

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.activityViewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.ferroli.after_sales.databinding.ActivityMainBinding
import com.ferroli.after_sales.entity.VersionInfo
import com.ferroli.after_sales.salesOrder.SalesOrderViewModel
import com.ferroli.after_sales.utils.DownLoadService

class MainActivity : AppCompatActivity() {
    private val viewModel by viewModels<MainViewModel>()
//    private val viewModelSo by viewModels<SalesOrderViewModel>()

    private lateinit var binding: ActivityMainBinding
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var navController: NavController

    private var verName: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.main_nav_host_fragment) as NavHostFragment

        navController = navHostFragment.navController

        val set = setOf(
            R.id.secMainFragment,
            R.id.agentOrderFragment
        )

        appBarConfiguration = AppBarConfiguration(set, binding.drawerLayoutMain)

        setupActionBarWithNavController(navController, appBarConfiguration)
        binding.mainNavigationView.setupWithNavController(navController)

        supportActionBar?.setBackgroundDrawable(
            ResourcesCompat.getDrawable(resources, R.drawable.ic_image_toolbar_background, theme)
        )

        try {
            val info = this.packageManager?.getPackageInfo(this.packageName!!, 0)
            if (info != null) {
                verName = info.versionName
            }
        } catch (e: Exception) {
            Log.d("Ferroli Log", e.toString())
        }

        viewModel.versionInfo.observe(this) {
            if (it.viVerName != this.verName) {
//                SilentUpdate.update {
//                    this.apkUrl = it.viUrl
//                    this.latestVersion = it.viVerName
//                    this.title = it.viTitle
//                    this.msg = it.viMsg
//                    this.isForce = true
//                    this.extra = hashMapOf<String, Any>()
//                }
                showUpdateDialog(it)
            } else {
                Toast.makeText(this, "已是最新版本", Toast.LENGTH_LONG).show()
            }
        }

        viewModel.getLastVersionData()

//        viewModelSo.getProvince()
//        viewModelSo.getCity()
//        viewModelSo.getDistrict()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.general_menu, menu)
        menu?.findItem(R.id.refreshGeneralMenu)?.isVisible = false

        return true
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    private fun showUpdateDialog(verInfo: VersionInfo) {
        val build = AlertDialog.Builder(this)
        build.setTitle(verInfo.viTitle)
        build.setMessage(verInfo.viMsg)
//        build.setPositiveButton(
//            "否"
//        ) { dialog, _ -> dialog.dismiss() }
        build.setNegativeButton("下载安装") { _, _ ->
            val intentService = Intent(this, DownLoadService::class.java)
            intentService.putExtra(
                "url",
                verInfo.viUrl
            )
            startService(intentService)
        }
        build.create().show()
    }
}