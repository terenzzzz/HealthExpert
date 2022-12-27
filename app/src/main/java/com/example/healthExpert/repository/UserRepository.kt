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
import com.example.healthExpert.viewmodels.UserViewModel
import com.google.gson.Gson
import okhttp3.*
import java.io.IOException


class UserRepository private constructor(private val activity: AppCompatActivity ) {

    private val client = OkHttpClient()
    private val BASE_URL = "http://terenzzzz.com:88"
    private val userViewModel: UserViewModel by lazy {
        ViewModelProvider(activity).get(UserViewModel::class.java)
    }

    companion object {
        private var instance: UserRepository? = null

        fun getInstance(activity: AppCompatActivity): UserRepository {
            if (instance == null) {
                instance = UserRepository(activity)
            }
            return instance!!
        }
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
                // 更新用户Id
                (userViewModel.idUser as MutableLiveData).postValue(parsed.idUser)
                // 保存Token到本地SharedPreferences
                val sharedPreferences: SharedPreferences =
                    activity.getSharedPreferences("healthy_expert", MODE_PRIVATE)
                sharedPreferences.edit()
                    .putString("token", parsed.token)
                    .commit()
                response.close()
            }
        })
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

    fun getUserInfo(idUser:Int) {
        val request = Request.Builder()
            .url("$BASE_URL/api/userInfo?idUser=$idUser")
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                e.printStackTrace()
            }

            override fun onResponse(call: Call, response: Response) {
                response.use {
//                    Log.d("getUser", response.body!!.string())
                    val gson = Gson()
                    val parsed: UserInfoParse = gson.fromJson(response.body!!.string(), UserInfoParse::class.java)
//                    // 更新viewmodel token
                    (userViewModel.email as MutableLiveData).postValue(parsed.data?.email ?: "")
                    (userViewModel.name as MutableLiveData).postValue(parsed.data?.name ?: "")
                    (userViewModel.age as MutableLiveData).postValue(parsed.data?.age ?: 0)
//                    (userViewModel.height as MutableLiveData).postValue((parsed.data?.height ?: 0f) as Float?)
//                    (userViewModel.weight as MutableLiveData).postValue((parsed.data?.weight ?: 0f) as Float?)
                    response.close()
                }
            }
        })
    }


}