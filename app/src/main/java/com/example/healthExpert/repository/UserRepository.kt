package com.example.healthExpert.repository

import android.content.SharedPreferences
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.healthExpert.R
import com.example.healthExpert.model.User
import com.example.healthExpert.parse.LoginParse
import com.example.healthExpert.parse.UserInfoParse
import com.example.healthExpert.view.home.Home
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import okhttp3.*
import java.io.IOException
import kotlin.math.pow

class UserRepository {
    private val client = OkHttpClient()

    suspend  fun getUserInfo(token:String):User {
        var user = User()
        val request = Request.Builder()
            .url("http://terenzzzz.com:88/my/userInfo")
            .addHeader("Authorization",token)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                e.printStackTrace()
            }

            override fun onResponse(call: Call, response: Response) {
                response.use {
                    val gson = Gson()
                    val parsed: UserInfoParse = gson.fromJson(response.body!!.string(), UserInfoParse::class.java)
//                    val bmi = parsed.data?.weight?.div((parsed.data?.height?.div(100)?.pow(2.0f))?.toFloat()!!)
                    Log.d("getUserInfo", parsed.data?.idUser.toString())
                    user.idUser = parsed.data?.idUser!!
                    user.Email = parsed.data?.email!!
                    user.Name = parsed.data?.name?: ""
                    user.Age = parsed.data?.age?: 0
                    user.Height = parsed.data?.height?: 0f
                    user.Weight = parsed.data?.weight?: 0f
                    user.Bmi = parsed.data?.bmi?: 0f
                    user.BodyFactRate = parsed.data?.bodyFatRate?: 0f
                    response.close()
                }
            }
        })
        return user
    }
}