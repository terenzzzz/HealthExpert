package com.example.healthExpert.repository

import android.util.Log
import com.example.healthExpert.model.Calories
import com.example.healthExpert.model.User
import com.example.healthExpert.parse.CaloriesInfoParse
import com.example.healthExpert.parse.UserInfoParse
import com.google.gson.Gson
import okhttp3.OkHttpClient
import okhttp3.Request
import kotlin.math.log

class CaloriesRepository {
    private val client = OkHttpClient()

    // 同步请求
    fun getCaloriesInfo(token:String): MutableList<Calories> {
        Log.w("CaloriesViewModel", "Request Called ", )
        var calories: MutableList<Calories> = mutableListOf()
        val request = Request.Builder()
            .url("http://terenzzzz.com:88/my/caloriesInfo")
            .addHeader("Authorization",token)
            .get()
            .build()

        client.newCall(request).execute().use { response ->
            val gson = Gson()
            val parsed: CaloriesInfoParse = gson.fromJson(response.body!!.string(), CaloriesInfoParse::class.java)
            Log.w("CaloriesViewModel", "status: "+parsed.status)
            Log.w("CaloriesViewModel", "message: " + parsed.message)
            if (parsed.data != null){
                for (caloriesInfo in parsed.data!!){
                    calories.add(caloriesInfo)
                }
            }
            response.close()
        }
        return calories
    }
}