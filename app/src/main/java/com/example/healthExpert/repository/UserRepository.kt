package com.example.healthExpert.repository

import android.util.Log
import com.example.healthExpert.model.User
import com.example.healthExpert.parse.UserInfoParse
import com.google.gson.Gson
import okhttp3.*
import java.io.IOException

class UserRepository {
    private val client = OkHttpClient()

    fun getUserInfo(token:String):User {
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
//                    Log.d("getUser", response.body!!.string())
                    val gson = Gson()
                    val parsed: UserInfoParse = gson.fromJson(response.body!!.string(), UserInfoParse::class.java)
                    Log.d("User", "idUser" + parsed.data?.idUser.toString())
                    Log.d("User", "email" + parsed.data?.email.toString())
                    Log.d("User", "name" + parsed.data?.name.toString())
                    Log.d("User", "age" + parsed.data?.age.toString())
                    Log.d("User", "height" + parsed.data?.height.toString())
                    Log.d("User", "weight" + parsed.data?.weight.toString())

                    user.idUser= parsed.data?.idUser!!
                    user.Email= parsed.data?.email!!
                    user.Name= parsed.data?.name?: ""
                    user.Age= parsed.data?.age?: 0
                    user.Height= parsed.data?.height?: 0f
                    user.Weight= parsed.data?.weight?: 0f

                    response.close()
                }
            }
        })
        return user
    }
}