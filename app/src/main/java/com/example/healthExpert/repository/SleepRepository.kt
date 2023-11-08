package com.example.healthExpert.repository

import android.util.Log
import com.example.healthExpert.parse.BaseParse
import com.example.healthExpert.parse.SleepParse
import com.example.healthExpert.parse.SleepsParse
import com.google.gson.Gson
import okhttp3.*
import java.io.IOException

class SleepRepository {
    private val client = OkHttpClient()
    private val url = "http://terenzzzz.cn:88/my"

    // 同步请求
    fun getSleep(token: String,date:String): SleepParse {
        var parsed = SleepParse()
        val request = Request.Builder()
            .url("$url/sleep?date=$date")
            .addHeader("Authorization",token)
            .get()
            .build()
        try {
            client.newCall(request).execute().use { response ->
                val gson = Gson()
                parsed = gson.fromJson(response.body!!.string(), SleepParse::class.java)
                response.close()
            }
        }catch (e:IOException){}
        return parsed
    }

    // 同步请求
    fun getLastFiveSleep(token: String): SleepsParse {
        var parsed = SleepsParse()
        val request = Request.Builder()
            .url("$url/lastFiveSleep")
            .addHeader("Authorization",token)
            .get()
            .build()
        try {
            client.newCall(request).execute().use { response ->
                val gson = Gson()
                parsed = gson.fromJson(response.body!!.string(), SleepsParse::class.java)
                response.close()
            }
        }catch (e:IOException){}
        return parsed
    }

    // 异步请求
    fun addSleep(
        token: String, temperature: Float, pressure: Float, light: Float,
        humidity: Float, startTime: String, callback: (Int) -> Unit
    ) {
        val body = FormBody.Builder()
            .add("temperature", temperature.toString())
            .add("pressure", pressure.toString())
            .add("light", light.toString())
            .add("humidity", humidity.toString())
            .add("startTime", startTime)
            .build()

        val request = Request.Builder()
            .url("$url/addSleep")
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
                    val resStatus = parsed.status
                    Log.w("SleepRepository", "添加Sleep")
                    callback(resStatus)
                }
            }
        })
    }
}