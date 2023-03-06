package com.example.healthExpert.repository

import android.util.Log
import com.example.healthExpert.model.Sleep
import com.example.healthExpert.parse.SleepParse
import com.google.gson.Gson
import okhttp3.OkHttpClient
import okhttp3.Request

class SleepRepository {
    private val client = OkHttpClient()
    private val url = "http://terenzzzz.com:88/my"

    // 同步请求
    fun getSleep(token: String): Sleep? {
        var sleep: Sleep? = null
        val request = Request.Builder()
            .url("$url/sleep")
            .addHeader("Authorization",token)
            .get()
            .build()

        client.newCall(request).execute().use { response ->
//            Log.d("getSleep", response.body.toString())
            val gson = Gson()
            val parsed: SleepParse = gson.fromJson(response.body!!.string(), SleepParse::class.java)
            if (parsed.data != null){
                sleep = parsed.data
            }
            response.close()
        }
        return sleep
    }
}