package com.example.healthExpert.repository

import android.util.Log
import com.example.healthExpert.model.User
import com.example.healthExpert.parse.BaseParse
import com.example.healthExpert.parse.UserInfoParse
import com.google.gson.Gson
import okhttp3.*
import java.io.IOException

class UserRepository {
    private val client = OkHttpClient()

    // 同步请求
    fun getUserInfo(token:String):User {
        var user = User()
        val request = Request.Builder()
        .url("http://terenzzzz.com:88/my/userInfo")
        .addHeader("Authorization",token)
        .build()

        client.newCall(request).execute().use { response ->
            val gson = Gson()
            val parsed: UserInfoParse = gson.fromJson(response.body!!.string(), UserInfoParse::class.java)
            Log.w("getUserInfo", "userId: "+parsed.data?.idUser.toString())
            user.idUser = parsed.data?.idUser!!
            user.Email = parsed.data?.email!!
            user.Name = parsed.data?.name?: ""
            user.Gender = parsed.data?.gender?: ""
            user.Age = parsed.data?.age?: 0
            user.Height = parsed.data?.height?: 0f
            user.Weight = parsed.data?.weight?: 0f
            user.Bmi = parsed.data?.bmi?: 0f
            user.BodyFactRate = parsed.data?.bodyFatRate?: 0f
            response.close()
        }
        return user
    }

    fun editName(token:String,name:String):Int {
        var resStatus=-1
        val body = FormBody.Builder()
            .add("name", name)
            .build()

        val request = Request.Builder()
            .url("http://terenzzzz.com:88/my/editName")
            .addHeader("Authorization",token)
            .post(body)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                e.printStackTrace()
            }
            override fun onResponse(call: Call, response: Response) {
                response.use {
                    val gson = Gson()
                    val parsed: BaseParse = gson.fromJson(response.body!!.string(), BaseParse::class.java)
                    Log.d("editName", parsed.status.toString())
                    resStatus = parsed.status?:-1
                    response.close()
                }
            }
        })
        return resStatus
    }

    fun editGender(token:String,gender:String):Int {
        var resStatus=-1
        val body = FormBody.Builder()
            .add("gender", gender)
            .build()

        val request = Request.Builder()
            .url("http://terenzzzz.com:88/my/editGender")
            .addHeader("Authorization",token)
            .post(body)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                e.printStackTrace()
            }
            override fun onResponse(call: Call, response: Response) {
                response.use {
                    val gson = Gson()
                    val parsed: BaseParse = gson.fromJson(response.body!!.string(), BaseParse::class.java)
                    Log.d("editGender", parsed.status.toString())
                    resStatus = parsed.status?:-1
                    response.close()
                }
            }
        })
        return resStatus
    }

    fun editAge(token:String,age:Int):Int {
        var resStatus=-1
        val body = FormBody.Builder()
            .add("age", age.toString())
            .build()

        val request = Request.Builder()
            .url("http://terenzzzz.com:88/my/editAge")
            .addHeader("Authorization",token)
            .post(body)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                e.printStackTrace()
            }
            override fun onResponse(call: Call, response: Response) {
                response.use {
                    val gson = Gson()
                    val parsed: BaseParse = gson.fromJson(response.body!!.string(), BaseParse::class.java)
                    Log.d("editAge", parsed.status.toString())
                    resStatus = parsed.status?:-1
                    response.close()
                }
            }
        })
        return resStatus
    }

    fun editHeight(token:String,height:Float):Int {
        var resStatus=-1
        val body = FormBody.Builder()
            .add("height", height.toString())
            .build()

        val request = Request.Builder()
            .url("http://terenzzzz.com:88/my/editHeight")
            .addHeader("Authorization",token)
            .post(body)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                e.printStackTrace()
            }
            override fun onResponse(call: Call, response: Response) {
                response.use {
                    val gson = Gson()
                    val parsed: BaseParse = gson.fromJson(response.body!!.string(), BaseParse::class.java)
                    Log.d("editHeight", parsed.status.toString())
                    resStatus = parsed.status?:-1
                    response.close()
                }
            }
        })
        return resStatus
    }

    fun editWeight(token:String,weight:Float):Int {
        var resStatus=-1
        val body = FormBody.Builder()
            .add("weight", weight.toString())
            .build()

        val request = Request.Builder()
            .url("http://terenzzzz.com:88/my/editWeight")
            .addHeader("Authorization",token)
            .post(body)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                e.printStackTrace()
            }
            override fun onResponse(call: Call, response: Response) {
                response.use {
                    val gson = Gson()
                    val parsed: BaseParse = gson.fromJson(response.body!!.string(), BaseParse::class.java)
                    Log.d("editWeight", parsed.status.toString())
                    resStatus = parsed.status?:-1
                    response.close()
                }
            }
        })
        return resStatus
    }
}