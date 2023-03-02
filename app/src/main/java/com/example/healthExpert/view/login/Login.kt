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
import com.example.healthExpert.view.home.Home
import com.example.healthExpert.view.resetPwd.ResetPwd
import com.example.healthExpert.view.signup.Signup
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import okhttp3.*
import java.io.IOException


class Login : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding

    companion object {
        fun startFn(context: Context) {
            val intent =
                Intent(context, Login::class.java)
            context.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
//        window.setBackgroundDrawable(null)
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
        getNotificationPermission()

        // Retrieve Token from SharedPreferences
        val sharedPreferences = getSharedPreferences("healthy_expert", MODE_PRIVATE)
        val email = sharedPreferences.getString("email","")
        val password = sharedPreferences.getString("password","")
        val token = sharedPreferences.getString("token","")
        if (token != null && token.isNotEmpty()) {
            Home.startFn(this)
            finish()
        }
        if (email != ""){
            binding.etEmail.text = Editable.Factory.getInstance().newEditable(email)
        }
        if (password != ""){
            binding.etPassword.text = Editable.Factory.getInstance().newEditable(password)
        }



        binding.logInBtn.setOnClickListener (View.OnClickListener { view ->
            Log.d("Login", "Loginbtn: Clicked")
            login(binding.etEmail.text.toString(),binding.etPassword.text.toString())
        })


        binding.signUpBtn.setOnClickListener (View.OnClickListener { view ->
            Signup.startFn(this)
        })

        binding.resetBtn.setOnClickListener (View.OnClickListener { view ->
            ResetPwd.startFn(this)
        })
    }

    private fun login(email:String, password:String){
        val client = OkHttpClient()
        val body = FormBody.Builder()
            .add("email", email)
            .add("password", password)
            .build()
//
        val request: Request = Request.Builder()
            .url("http://terenzzzz.com:88/api/login")
            .post(body)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Snackbar.make(binding.root, "Network Error!", Snackbar.LENGTH_LONG).show()
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

    private fun getNotificationPermission() {
        if (Build.VERSION.SDK_INT >= 26) {
            val intent = Intent()
            intent.action = Settings.ACTION_APP_NOTIFICATION_SETTINGS
            intent.putExtra(Settings.EXTRA_APP_PACKAGE, this.packageName)
            this.startActivity(intent)
        } else {
            val intent = Intent()
            intent.action = "android.settings.APP_NOTIFICATION_SETTINGS"
            intent.putExtra("app_package", this.applicationContext.packageName)
            intent.putExtra("app_uid", this.applicationInfo.uid)
            this.startActivity(intent)
        }
    }

}