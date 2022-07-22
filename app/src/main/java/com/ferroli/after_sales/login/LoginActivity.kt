package com.ferroli.after_sales.login

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.ferroli.after_sales.MainActivity
import com.ferroli.after_sales.R
import com.ferroli.after_sales.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val viewModel by viewModels<LoginViewModel>()

        viewModel.refreshUserInfo()

        binding.tbUsernameLogin.setText(viewModel.empName.value)

        binding.btnLoginLogin.setOnClickListener {
            binding.pbWaitingLogin.visibility = View.VISIBLE

            val userName: String = binding.tbUsernameLogin.text.toString()
            val password: String = binding.tbPasswordLogin.text.toString()

            if (userName.isEmpty() || password.isEmpty()) {
                Toast.makeText(
                    applicationContext,
                    application.resources.getString(R.string.login_hint_5),
                    Toast.LENGTH_LONG
                ).show()
                binding.pbWaitingLogin.visibility = View.GONE
                return@setOnClickListener
            }

            viewModel.loginUser(userName, password)
        }

        viewModel.userAccount.observe(this) {
            if (it != null) {
                viewModel.saveUserInfo(it)

                val intent = Intent(this, MainActivity::class.java)
                intent.flags =
                    Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(intent)
            } else {
                Toast.makeText(
                    applicationContext,
                    application.resources.getString(R.string.login_hint_5),
                    Toast.LENGTH_LONG
                ).show()
            }

            binding.pbWaitingLogin.visibility = View.GONE
        }
    }

}