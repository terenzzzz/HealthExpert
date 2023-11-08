package com.example.healthExpert.view.login

import android.Manifest
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.text.Editable
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.healthExpert.R
import com.example.healthExpert.databinding.ActivityLoginBinding
import com.example.healthExpert.parse.LoginParse
import com.example.healthExpert.utils.SnackbarUtil
import com.example.healthExpert.view.home.Home
import com.example.healthExpert.view.onBoarding.OnBoarding
import com.example.healthExpert.view.resetPwd.ResetPwd
import com.example.healthExpert.view.signup.Signup
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import okhttp3.*
import java.io.IOException


class                                                                                                                          Login : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding

    companion object {
        fun startFn(context: Context) {
            val intent =
                Intent(context, Login::class.java)
            context.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Permission Check
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACTIVITY_RECOGNITION)
            != PackageManager.PERMISSION_GRANTED) {
            // Permission is not granted
            ActivityCompat.requestPermissions(this,
                arrayOf(Manifest.permission.ACTIVITY_RECOGNITION),
                5)
        }
//        getNotificationPermission()

        // Retrieve Token from SharedPreferences
        val sharedPreferences = getSharedPreferences("healthy_expert", MODE_PRIVATE)
        val email = sharedPreferences.getString("email","")
        val password = sharedPreferences.getString("password","")
        val token = sharedPreferences.getString("token","")
        val isOnBoarded = sharedPreferences.getBoolean("onBoarded",false)

        if (!isOnBoarded){
            OnBoarding.startFn(this)
            finish()
        }

        // refill User identity
        if (email != "" && password != ""){
            if (token != null && token.isNotEmpty()) {
                if (email != null && password != null) {
                    login(email,password)
                }
            }
            binding.etEmail.text = Editable.Factory.getInstance().newEditable(email)
            binding.etPassword.text = Editable.Factory.getInstance().newEditable(password)
            binding.checkbox.isChecked = true
        }

        binding.logInBtn.setOnClickListener { view ->
            login(binding.etEmail.text.toString(),binding.etPassword.text.toString())
        }

        binding.signUpBtn.setOnClickListener {
            Signup.startFn(this)
        }

        binding.resetBtn.setOnClickListener {
            ResetPwd.startFn(this)
        }
    }

    private fun login(email:String, password:String){
        val client = OkHttpClient()
        val body = FormBody.Builder()
            .add("email", email)
            .add("password", password)
            .build()
//
        val request: Request = Request.Builder()
            .url("http://terenzzzz.cn:88/api/login")
            .post(body)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                SnackbarUtil.buildNetwork(binding.root)
            }

            override fun onResponse(call: Call, response: Response) {
                val gson = Gson()
                val parsed: LoginParse = gson.fromJson(response.body!!.string(), LoginParse::class.java)
                // 保存Token到本地SharedPreferences
                val sharedPreferences: SharedPreferences =
                    this@Login.getSharedPreferences("healthy_expert", MODE_PRIVATE)
                if (parsed.status == 200){
                    // 保存账号密码到本地SharedPreferences
                    sharedPreferences.edit()
                        .putString("email", binding.etEmail.text.toString())
                        .putString("password", binding.etPassword.text.toString())
                        .putString("token", parsed.token)
                        .putString("idUser", parsed.idUser.toString())
                        .putBoolean("remember", binding.checkbox.isChecked)
                        .commit()
                    Home.startFn(this@Login)
                    finish()
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
                }else{
                    Snackbar.make(binding.root, "Log in Fail!", Snackbar.LENGTH_LONG).show()
                }
                response.close()
            }
        })
    }
}