package com.ferroli.after_sales

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import com.ferroli.after_sales.databinding.ActivityLauncherBinding
import com.ferroli.after_sales.login.LoginActivity

class LauncherActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLauncherBinding
    private lateinit var topAnim: Animation
    private lateinit var bottomAnim: Animation
    private val splashScreen: Long = 3000

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLauncherBinding.inflate(layoutInflater)
        setContentView(binding.root)

        topAnim = AnimationUtils.loadAnimation(this, R.anim.top_animation)
        bottomAnim = AnimationUtils.loadAnimation(this, R.anim.bottom_animation)

        binding.imgTopLauncher.animation = topAnim
        binding.imgBottomLauncher.animation = bottomAnim

        Handler(Looper.getMainLooper()).postDelayed({
            checkUserInfo()
        }, splashScreen)
    }

    private fun checkUserInfo() {
        val intent = Intent(this, LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)

//        val shpName = application.resources.getString(R.string.shp_name)
//        val keyEmpId: String =
//            application.resources.getString(R.string.shp_emp_id)
//
//        val shp: SharedPreferences =
//            application.getSharedPreferences(shpName, Context.MODE_PRIVATE)
//        val empId: Int = shp.getInt(keyEmpId, 0)
//
//        if (empId == 0) {
//            val intent = Intent(this, LoginActivity::class.java)
//            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
//            startActivity(intent)
//        } else {
//            val intent = Intent(this, MainActivity::class.java)
//            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
//            startActivity(intent)
//        }
    }
}