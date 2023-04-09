package com.example.healthExpert.repository

import android.util.Log
import com.example.healthExpert.parse.BaseParse
import com.example.healthExpert.parse.CaloriesOverallParse
import com.example.healthExpert.parse.CaloriesParse
import com.example.healthExpert.parse.GoalParse
import com.google.gson.Gson
import okhttp3.*
import java.io.IOException

class GoalsRepository {
    private val client = OkHttpClient()
    private val url = "http://terenzzzz.com:88/my"


    // 同步请求
    fun getGoal(token:String): GoalParse {
        var parsed = GoalParse()
        val request = Request.Builder()
            .url("$url/goal")
            .addHeader("Authorization",token)
            .get()
            .build()
        try {
            client.newCall(request).execute().use { response ->
                val gson = Gson()
                parsed = gson.fromJson(response.body!!.string(), GoalParse::class.java)
                response.close()
            }
        } catch (e:IOException){ }
        return parsed
    }

    // 异步请求
    fun initGoals(token: String):Int{
        var resStatus=-1
        val body = FormBody.Builder().build()

        val request = Request.Builder()
            .url("$url/initGoals")
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
                    resStatus = parsed.status
                    response.close()
                }
            }
        })
        return resStatus
    }

    // 同步
    fun editCalories(token: String, calories: String):Int {
        var resStatus=-1
        val body = FormBody.Builder()
            .add("calories", calories)
            .build()

        val request = Request.Builder()
            .url("$url/editCalories")
            .addHeader("Authorization",token)
            .post(body)
            .build()

        try {
            client.newCall(request).execute().use { response ->
                val gson = Gson()
                val parsed: BaseParse =
                    gson.fromJson(response.body!!.string(), BaseParse::class.java)
                resStatus = parsed.status
                response.close()
            }
        } catch (e: IOException) { }
        return resStatus
    }

    fun editSteps(token: String, steps: String):Int {
        var resStatus=-1
        val body = FormBody.Builder()
            .add("steps", steps)
            .build()

        val request = Request.Builder()
            .url("$url/editSteps")
            .addHeader("Authorization",token)
            .post(body)
            .build()

        try {
            client.newCall(request).execute().use { response ->
                val gson = Gson()
                val parsed: BaseParse = gson.fromJson(response.body!!.string(), BaseParse::class.java)
                resStatus = parsed.status
                response.close()
            }
        } catch (e: IOException) { }
        return resStatus
    }

    fun editTraining(token: String, training: String):Int {
        var resStatus=-1
        val body = FormBody.Builder()
            .add("training", training)
            .build()

        val request = Request.Builder()
            .url("$url/editTraining")
            .addHeader("Authorization",token)
            .post(body)
            .build()

        try {
            client.newCall(request).execute().use { response ->
                val gson = Gson()
                val parsed: BaseParse = gson.fromJson(response.body!!.string(), BaseParse::class.java)
                resStatus = parsed.status
                response.close()
            }
        } catch (e: IOException) { }
        return resStatus
    }

    fun editWater(token: String, water: String):Int {
        var resStatus=-1
        val body = FormBody.Builder()
            .add("water", water)
            .build()

        val request = Request.Builder()
            .url("$url/editWater")
            .addHeader("Authorization",token)
            .post(body)
            .build()

        try {
            client.newCall(request).execute().use { response ->
                val gson = Gson()
                val parsed: BaseParse = gson.fromJson(response.body!!.string(), BaseParse::class.java)
                resStatus = parsed.status
                response.close()
            }
        } catch (e: IOException) {}

        return resStatus
    }

    fun editSleep(token: String, sleep: String):Int {
        var resStatus=-1
        val body = FormBody.Builder()
            .add("sleep", sleep)
            .build()

        val request = Request.Builder()
            .url("$url/editSleep")
            .addHeader("Authorization",token)
            .post(body)
            .build()

        try {
            client.newCall(request).execute().use { response ->
                val gson = Gson()
                val parsed: BaseParse =
                    gson.fromJson(response.body!!.string(), BaseParse::class.java)
                resStatus = parsed.status
                response.close()
            }
        }catch (e: IOException) {}
        return resStatus
    }
}

