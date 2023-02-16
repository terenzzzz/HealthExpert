package com.example.healthExpert.repository

import android.util.Log
import com.example.healthExpert.model.Calories
import com.example.healthExpert.model.WalkStep
import com.example.healthExpert.model.Walks
import com.example.healthExpert.parse.CaloriesParse
import com.example.healthExpert.parse.WalkStepsParse
import com.example.healthExpert.parse.WalksParse
import com.google.gson.Gson
import okhttp3.OkHttpClient
import okhttp3.Request


class WalkRepository {
    private val client = OkHttpClient()

    // 同步请求
    fun getWalks(token:String,date: String): Walks {
        var walk = Walks()
        val request = Request.Builder()
            .url("http://terenzzzz.com:88/my/walks?date=$date")
            .addHeader("Authorization",token)
            .get()
            .build()

        client.newCall(request).execute().use { response ->
            val gson = Gson()
            val parsed: WalksParse = gson.fromJson(response.body!!.string(), WalksParse::class.java)
            if (parsed.data != null){
                walk = parsed.data!!
            }
            Log.w("getWalks", "当天行走信息: $walk")
            response.close()
        }
        return walk
    }

    fun getWalkStep(token:String,idWalk: Int): MutableList<WalkStep> {
        var walkSteps: MutableList<WalkStep> = mutableListOf()
        val request = Request.Builder()
            .url("http://terenzzzz.com:88/my/walkSteps?idWalk=$idWalk")
            .addHeader("Authorization",token)
            .get()
            .build()

        client.newCall(request).execute().use { response ->
            val gson = Gson()
            val parsed: WalkStepsParse = gson.fromJson(response.body!!.string(), WalkStepsParse::class.java)
            if (parsed.data != null){
                for (step in parsed.data!!)
                walkSteps.add(step)
            }
            Log.w("getWalkStep", "当天行走步数信息: $walkSteps")
            response.close()
        }
        return walkSteps
    }
}