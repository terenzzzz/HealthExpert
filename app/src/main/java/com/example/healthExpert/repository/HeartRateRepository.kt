package com.example.healthExpert.repository

import android.util.Log
import com.example.healthExpert.parse.BaseParse
import com.example.healthExpert.parse.CaloriesOverallParse
import com.example.healthExpert.parse.CaloriesParse
import com.example.healthExpert.parse.HeartRateParse
import com.google.gson.Gson
import okhttp3.*
import java.io.IOException

class HeartRateRepository {
    private val client = OkHttpClient()
    private val url = "http://terenzzzz.com:88/my"


    // 同步请求
    fun getHeartRates(token:String,date:String): HeartRateParse {
        var parsed = HeartRateParse()
        val request = Request.Builder()
            .url("$url/heartRates?date=$date")
            .addHeader("Authorization",token)
            .get()
            .build()
        try {
            client.newCall(request).execute().use { response ->
                val gson = Gson()
                parsed = gson.fromJson(response.body!!.string(), HeartRateParse::class.java)
                response.close()
            }
        } catch (e:IOException){

        }
        return parsed
    }

    fun addHeartRate(token: String,heartRate:String,callback: (Int) -> Unit) {
        val body = FormBody.Builder()
            .add("heartRate", heartRate)
            .build()

        val request = Request.Builder()
            .url("$url/addHeartRate")
            .addHeader("Authorization",token)
            .post(body)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                e.printStackTrace()
                callback(-1)
            }

            override fun onResponse(call: Call, response: Response) {
                response.use {
                    val gson = Gson()
                    val parsed: BaseParse = gson.fromJson(response.body!!.string(), BaseParse::class.java)
                    callback(parsed.status)
                    response.close()
                }
            }
        })
    }

}

