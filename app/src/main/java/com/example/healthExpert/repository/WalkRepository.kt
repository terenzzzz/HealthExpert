package com.example.healthExpert.repository

import android.util.Log
import com.example.healthExpert.model.WalkStep
import com.example.healthExpert.model.Walks
import com.example.healthExpert.parse.BaseParse
import com.example.healthExpert.parse.WalkStepsParse
import com.example.healthExpert.parse.WalksParse
import com.google.gson.Gson
import okhttp3.*
import java.io.IOException


class WalkRepository {
    private val client = OkHttpClient()

    // 同步请求
    fun getWalks(token:String): Walks {
        var walk = Walks()
        val request = Request.Builder()
            .url("http://terenzzzz.com:88/my/walks")
            .addHeader("Authorization",token)
            .get()
            .build()

        client.newCall(request).execute().use { response ->
            val gson = Gson()
            val parsed: WalksParse = gson.fromJson(response.body!!.string(), WalksParse::class.java)
            if (parsed.data != null){
//                Log.d("getWalks", parsed.data.toString())
                walk = parsed.data!!
            }
            Log.w("getWalks", "当天行走信息: $walk")
            response.close()
        }
        return walk
    }

    fun getWalkStep(token:String): MutableList<WalkStep> {
        var walkSteps: MutableList<WalkStep> = mutableListOf()
        val request = Request.Builder()
            .url("http://terenzzzz.com:88/my/walkSteps")
            .addHeader("Authorization",token)
            .get()
            .build()

        client.newCall(request).execute().use { response ->
            val gson = Gson()
            val parsed: WalkStepsParse = gson.fromJson(response.body!!.string(), WalkStepsParse::class.java)
            if (parsed.data != null){
                for (step in parsed.data!!){
                    walkSteps.add(step)
                }
            }
            Log.w("getWalkStep", "当天行走步数信息: $walkSteps")
            response.close()
        }
        return walkSteps
    }

    fun addWalk(token:String,steps:String):Int{
        var resStatus=-1
        val body = FormBody.Builder()
            .add("steps", steps)
            .build()

        val request = Request.Builder()
            .url("http://terenzzzz.com:88/my/addWalk")
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
                    Log.w("addWalk", "更新行走成功: $resStatus")
                    response.close()
                }
            }
        })
        return resStatus
    }

    fun addWalkSteps(token:String,steps:String):Int{
        var resStatus=-1
        val body = FormBody.Builder()
            .add("steps", steps)
            .build()

        val request = Request.Builder()
            .url("http://terenzzzz.com:88/my/addWalkSteps")
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
                    Log.w("addWalkSteps", "步数更新成功: $steps")
                    response.close()
                }
            }
        })
        return resStatus
    }
}