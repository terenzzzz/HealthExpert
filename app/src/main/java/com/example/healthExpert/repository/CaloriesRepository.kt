package com.example.healthExpert.repository

import android.util.Log
import com.example.healthExpert.model.Calories
import com.example.healthExpert.parse.BaseParse
import com.example.healthExpert.parse.CaloriesInfoParse
import com.google.gson.Gson
import okhttp3.*
import java.io.IOException

class CaloriesRepository {
    private val client = OkHttpClient()

    // 同步请求
    fun getCaloriesInfo(token:String): MutableList<Calories> {
        var calories: MutableList<Calories> = mutableListOf()
        val request = Request.Builder()
            .url("http://terenzzzz.com:88/my/caloriesInfo")
            .addHeader("Authorization",token)
            .get()
            .build()

        client.newCall(request).execute().use { response ->
            val gson = Gson()
            val parsed: CaloriesInfoParse = gson.fromJson(response.body!!.string(), CaloriesInfoParse::class.java)
            Log.w("CaloriesRepository", "message: " + parsed.message)
            if (parsed.data != null){
                for (caloriesInfo in parsed.data!!){
                    calories.add(caloriesInfo)
                }
            }
            response.close()
        }
        return calories
    }

    // 异步请求
    fun addCalories(token:String,type:String,title:String,content:String,calories:Int,time:String):Int{
        var resStatus=-1
        val body = FormBody.Builder()
            .add("type", type)
            .add("title", title)
            .add("content", content)
            .add("calories", calories.toString())
            .add("time", time)
            .build()

        val request = Request.Builder()
            .url("http://terenzzzz.com:88/my/addCalories")
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
                    resStatus = parsed.status?:-1
                    Log.w("CaloriesRepository", "addCalories: $resStatus")
                    response.close()
                }
            }
        })
        return resStatus
    }
}