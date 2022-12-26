package com.example.healthExpert.repository

import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.healthExpert.parse.BaseParse
import com.example.healthExpert.parse.LoginParse
import com.example.healthExpert.view.login.Login
import com.example.healthExpert.viewmodels.UserViewModel
import com.google.gson.Gson
import okhttp3.*
import java.io.IOException


class UserRepository(private val activity: AppCompatActivity) {
    private val client = OkHttpClient()
    private val BASE_URL = "http://terenzzzz.com:88"
    private val userViewModel: UserViewModel by lazy {
        ViewModelProvider(activity).get(UserViewModel::class.java)
    }


    fun getUser(idUser:Int) {
        val request = Request.Builder()
            .url("$BASE_URL/api/user?idUser=$idUser")
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                e.printStackTrace()
            }

            override fun onResponse(call: Call, response: Response) {
                response.use {
                    Log.d("Login", response.body!!.string())
                }
            }
        })
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
                (userViewModel.signupStatus as MutableLiveData).postValue(parsed.status)
                response.close()
            }
        })
    }

    fun login(email:String, password:String){
        val body = FormBody.Builder()
            .add("email", email)
            .add("password", password)
            .build()
//
        val request: Request = Request.Builder()
            .url("$BASE_URL/api/login")
            .post(body)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                e.printStackTrace()
            }

            override fun onResponse(call: Call, response: Response) {
                val gson = Gson()
                val parsed: LoginParse = gson.fromJson(response.body!!.string(), LoginParse::class.java)
                // 更新viewmodel token
                (userViewModel.loginStatus as MutableLiveData).postValue(parsed.status)
                response.close()
            }
        })
    }
}