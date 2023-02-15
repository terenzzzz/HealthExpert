package com.example.healthExpert.repository

import android.util.Log
import com.example.healthExpert.model.Water
import com.example.healthExpert.parse.WaterParse
import com.google.gson.Gson
import okhttp3.OkHttpClient
import okhttp3.Request

class WatersRepository {
    private val client = OkHttpClient()

    // 同步请求
    fun getWaters(token: String): MutableList<Water> {
        var water: MutableList<Water> = mutableListOf()
        val request = Request.Builder()
            .url("http://terenzzzz.com:88/my/waters")
            .addHeader("Authorization",token)
            .get()
            .build()

        client.newCall(request).execute().use { response ->
            val gson = Gson()
            val parsed: WaterParse = gson.fromJson(response.body!!.string(), WaterParse::class.java)
            if (parsed.data != null){
                for (waterData in parsed.data!!){
                    water.add(waterData)
                }
            }
            Log.w("getWaters", water.toString())
            response.close()
        }
        return water
    }
}