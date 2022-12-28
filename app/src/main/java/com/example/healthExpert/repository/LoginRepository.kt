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
import com.google.gson.Gson
import okhttp3.*
import java.io.IOException


class LoginRepository (private val activity: AppCompatActivity ) {

    private val client = OkHttpClient()
    private val BASE_URL = "http://terenzzzz.com:88"
    private val loginViewModel: LoginViewModel by lazy {
        ViewModelProvider(activity).get(LoginViewModel::class.java)
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
                (loginViewModel.loginStatus as MutableLiveData).postValue(parsed.status)
                // 更新用户Id
                (loginViewModel.idUser as MutableLiveData).postValue(parsed.idUser)
                // 保存Token到本地SharedPreferences
                val sharedPreferences: SharedPreferences =
                    activity.getSharedPreferences("healthy_expert", MODE_PRIVATE)
                sharedPreferences.edit()
                    .putString("token", parsed.token)
                    .putString("idUser", parsed.idUser.toString())
                    .commit()
                response.close()
            }
        })
    }


//    fun signup(email:String, password:String,confirmPassword:String){
//        val body = FormBody.Builder()
//            .add("email", email)
//            .add("password", password)
//            .add("confirmPassword", confirmPassword)
//            .build()
////
//        val request: Request = Request.Builder()
//            .url("$BASE_URL/api/register")
//            .post(body)
//            .build()
//
//        client.newCall(request).enqueue(object : Callback {
//            override fun onFailure(call: Call, e: IOException) {
//                e.printStackTrace()
//            }
//
//            override fun onResponse(call: Call, response: Response) {
//                val gson = Gson()
//                val parsed: BaseParse = gson.fromJson(response.body!!.string(), BaseParse::class.java)
//                // 更新viewmodel token
//                (loginViewModel.signupStatus as MutableLiveData).postValue(parsed.status)
//            }
//        })
//    }



//    fun getUser(idUser:Int) {
//        val request = Request.Builder()
//            .url("$BASE_URL/api/user?idUser=$idUser")
//            .build()
//
//        client.newCall(request).enqueue(object : Callback {
//            override fun onFailure(call: Call, e: IOException) {
//                e.printStackTrace()
//            }
//
//            override fun onResponse(call: Call, response: Response) {
//                response.use {
//                    Log.d("Login", response.body!!.string())
//                }
//            }
//        })
//    }
//
//    fun getUserInfo(idUser:Int) {
//        val request = Request.Builder()
//            .url("$BASE_URL/api/userInfo?idUser=$idUser")
//            .build()
//
//        client.newCall(request).enqueue(object : Callback {
//            override fun onFailure(call: Call, e: IOException) {
//                e.printStackTrace()
//            }
//
//            override fun onResponse(call: Call, response: Response) {
//                response.use {
////                    Log.d("getUser", response.body!!.string())
//                    val gson = Gson()
//                    val parsed: UserInfoParse = gson.fromJson(response.body!!.string(), UserInfoParse::class.java)
////                    // 更新viewmodel token
//                    (loginViewModel.email as MutableLiveData).postValue(parsed.data?.email)
//                    (loginViewModel.name as MutableLiveData).postValue(parsed.data?.name)
//                    (loginViewModel.age as MutableLiveData).postValue(parsed.data?.age)
//                    (loginViewModel.height as MutableLiveData).postValue(parsed.data?.height)
//                    (loginViewModel.weight as MutableLiveData).postValue(parsed.data?.weight)
//                    response.close()
//                }
//            }
//        })
//    }


}