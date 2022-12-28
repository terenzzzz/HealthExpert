package com.example.healthExpert.repository

import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import com.example.healthExpert.parse.BaseParse
import com.example.healthExpert.parse.LoginParse
import com.example.healthExpert.parse.UserInfoParse
import com.example.healthExpert.viewmodels.LoginViewModel
import com.example.healthExpert.viewmodels.SignupViewModel
import com.google.gson.Gson
import okhttp3.*
import java.io.IOException


class SignupRepository (private val activity: AppCompatActivity ) {

    private val client = OkHttpClient()
    private val BASE_URL = "http://terenzzzz.com:88"
    private val signupViewModel: SignupViewModel by lazy {
        ViewModelProvider(activity).get(SignupViewModel::class.java)
    }


    fun signup(email:String, password:String,confirmPassword:String){
        val body = FormBody.Builder()
            .add("email", email)
            .add("password", password)
            .add("confirmPassword", confirmPassword)
            .build()
//
        val request: Request = Request.Builder()
            .url("$BASE_URL/api/register")
            .post(body)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                e.printStackTrace()
            }

            override fun onResponse(call: Call, response: Response) {
                val gson = Gson()
                val parsed: BaseParse = gson.fromJson(response.body!!.string(), BaseParse::class.java)
                // 更新viewmodel token
                (signupViewModel.signupStatus as MutableLiveData).postValue(parsed.status)
            }
        })
    }


}