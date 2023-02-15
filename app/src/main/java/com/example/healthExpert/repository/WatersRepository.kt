package com.example.healthExpert.repository

import android.util.Log
import com.example.healthExpert.model.Water
import com.example.healthExpert.parse.BaseParse
import com.example.healthExpert.parse.WaterParse
import com.google.gson.Gson
import okhttp3.*
import java.io.IOException

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

    // 异步请求
    fun addCalories(token:String,type:String,title:String,content:String,value:Int,time:String):Int{
        var resStatus=-1
        val body = FormBody.Builder()
            .add("type", type)
            .add("title", title)
            .add("content", content)
            .add("value", value.toString())
            .add("time", time)
            .build()

        val request = Request.Builder()
            .url("http://terenzzzz.com:88/my/addWaters")
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
                    Log.w("addCalories", "addCalories: $resStatus")
                    response.close()
                }
            }
        })
        return resStatus
    }
}