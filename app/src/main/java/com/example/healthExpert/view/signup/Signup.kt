package com.example.healthExpert.view.signup

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.healthExpert.R
import com.example.healthExpert.databinding.ActivitySignupBinding
import com.example.healthExpert.parse.BaseParse
import com.example.healthExpert.repository.GoalsRepository
import com.example.healthExpert.view.login.Login
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import okhttp3.*
import java.io.IOException

class Signup : AppCompatActivity() {
    private lateinit var binding: ActivitySignupBinding

    companion object {
        fun startFn(context: Context) {
            val intent =
                Intent(context, Signup::class.java)
            context.startActivity(intent)
            (context as Activity).overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.signUpBtn.setOnClickListener (View.OnClickListener { view ->
            signup(binding.etEmail.text.toString(),
                binding.etPassword.text.toString(),
                binding.etPasswordConfirm.text.toString())
        })

        binding.backBtn.setOnClickListener (View.OnClickListener { view ->
            Login.startFn(this)
        })
    }

    fun signup(email:String, password:String,confirmPassword:String){
        val client = OkHttpClient()
        val body = FormBody.Builder()
            .add("email", email)
            .add("password", password)
            .add("confirmPassword", confirmPassword)
            .build()
//
        val request: Request = Request.Builder()
            .url("http://terenzzzz.com:88/api/register")
            .post(body)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                e.printStackTrace()
            }

            override fun onResponse(call: Call, response: Response) {
                val gson = Gson()
                val parsed: BaseParse = gson.fromJson(response.body!!.string(), BaseParse::class.java)
                if (parsed.status == 200){
                    Snackbar.make(binding.root, "Signup Successfully!", Snackbar.LENGTH_LONG).show()
                    finish()
                }else{
                    Snackbar.make(binding.root, "Signup Fail!", Snackbar.LENGTH_LONG).show()
                }
            }
        })
    }
}