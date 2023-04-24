package com.example.healthExpert.repository

import android.util.Log
import com.example.healthExpert.model.WalkStep
import com.example.healthExpert.model.WalksOverall
import com.example.healthExpert.parse.BaseParse
import com.example.healthExpert.parse.CaloriesOverallParse
import com.example.healthExpert.parse.WalkStepsParse
import com.example.healthExpert.parse.WalksOverallParse
import com.google.gson.Gson
import okhttp3.*
import java.io.IOException


class WalkRepository {
    private val client = OkHttpClient()
    private val url = "http://terenzzzz.com:88/my"

    // 同步请求
    fun getWalksOverall(token:String,date:String): WalksOverallParse {
        var parsed = WalksOverallParse()
        val request = Request.Builder()
            .url("$url/walksOverall?date=$date")
            .addHeader("Authorization",token)
            .get()
            .build()

        try {
            client.newCall(request).execute().use { response ->
                val gson = Gson()
                parsed = gson.fromJson(response.body!!.string(), WalksOverallParse::class.java)
                response.close()
            }
        }catch (e: IOException){

        }
        return parsed
    }

    fun getWalkStep(token:String,date: String): WalkStepsParse {
        var parsed = WalkStepsParse()
        val request = Request.Builder()
            .url("$url/walkSteps?date=$date")
            .addHeader("Authorization",token)
            .get()
            .build()

        try {
            client.newCall(request).execute().use { response ->
                val gson = Gson()
                parsed = gson.fromJson(response.body!!.string(), WalkStepsParse::class.java)
                response.close()
            }
        }catch (e:IOException){
        }

        return parsed
    }

    fun updateWalksOverall(token:String,weight:Float,height:Float):Int{
        var resStatus=-1
        val body = FormBody.Builder()
            .add("weight", weight.toString())
            .add("height", height.toString())
            .build()

        val request = Request.Builder()
            .url("$url/updateWalksOverall")
            .addHeader("Authorization",token)
            .post(body)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                e.printStackTrace()
                resStatus = -1
            }

            override fun onResponse(call: Call, response: Response) {
                response.use {
//                    Log.d("测试", "updateWalksOverall: 数据：${response}")
                    val gson = Gson()
                    val parsed: BaseParse = gson.fromJson(response.body!!.string(), BaseParse::class.java)
                    resStatus = parsed.status?:-1
                    Log.d("测试", "updateWalksOverall: 成功!!!!!!!!!!!!!!!")
                    response.close()
                }
            }
        })
        return resStatus
    }

    fun addWalkSteps(token:String,steps:String, callback: (Int) -> Unit){
        val body = FormBody.Builder()
            .add("steps", steps)
            .build()

        val request = Request.Builder()
            .url("$url/addWalkSteps")
            .addHeader("Authorization",token)
            .post(body)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                callback.invoke(-1)
            }

            override fun onResponse(call: Call, response: Response) {
                response.use {
                    val gson = Gson()
                    val parsed: BaseParse = gson.fromJson(response.body!!.string(), BaseParse::class.java)
                    val resStatus = parsed.status?:-1
                    Log.w("addWalkSteps", "步数更新成功: $steps")
                    callback.invoke(resStatus)
                }
            }
        })
    }
}