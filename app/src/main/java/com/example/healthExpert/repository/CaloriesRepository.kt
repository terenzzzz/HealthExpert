package com.example.healthExpert.repository

import android.util.Log
import com.example.healthExpert.parse.BaseParse
import com.example.healthExpert.parse.CaloriesOverallParse
import com.example.healthExpert.parse.CaloriesParse
import com.google.gson.Gson
import okhttp3.*
import java.io.IOException

class CaloriesRepository {
    private val client = OkHttpClient()
    private val url = "http://terenzzzz.com:88/my"


    // 同步请求
    fun getCaloriesOverall(token:String,date:String): CaloriesOverallParse {
        var parsed = CaloriesOverallParse()
        val request = Request.Builder()
            .url("$url/caloriesOverall?date=$date")
            .addHeader("Authorization",token)
            .get()
            .build()
        try {
            client.newCall(request).execute().use { response ->
                val gson = Gson()
                parsed = gson.fromJson(response.body!!.string(), CaloriesOverallParse::class.java)
                response.close()
            }
        } catch (e:IOException){

        }
        return parsed
    }

    // 异步请求
    fun updateCaloriesOverall(token: String, param: (Any) -> Unit):Int{
        var resStatus=-1
        val body = FormBody.Builder().build()

        val request = Request.Builder()
            .url("$url/updateCaloriesOverall")
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
                    Log.w("CaloriesRepository", "更新CaloriesOverall")
                    response.close()
                }
            }
        })
        return resStatus
    }



    // 同步请求
    fun getCalories(token:String,date: String): CaloriesParse {
        var parsed = CaloriesParse()
        val request = Request.Builder()
            .url("$url/calories?date=$date")
            .addHeader("Authorization",token)
            .get()
            .build()

        try{
            client.newCall(request).execute().use { response ->
                val gson = Gson()
                parsed = gson.fromJson(response.body!!.string(), CaloriesParse::class.java)
                response.close()
            }
        }catch (e:IOException){ }

        return parsed
    }

    // 同步请求
    fun getCaloriesInfo(token:String,id:Int): CaloriesParse {
        var parsed = CaloriesParse()
        val request = Request.Builder()
            .url("$url/caloriesInfo?id=$id")
            .addHeader("Authorization",token)
            .get()
            .build()
        try{
            client.newCall(request).execute().use { response ->
                val gson = Gson()
                parsed = gson.fromJson(response.body!!.string(), CaloriesParse::class.java)
                response.close()
            }
        }catch (e:IOException){ }
        return parsed
    }

    // 异步请求
    fun addCalories(
        token: String,
        type: String,
        title: String,
        content: String,
        calories: Int,
        time: String,
        param: (Any) -> Unit
    ):Int{
        var resStatus = -1
        val body = FormBody.Builder()
            .add("type", type)
            .add("title", title)
            .add("content", content)
            .add("calories", calories.toString())
            .add("time", time)
            .build()

        val request = Request.Builder()
            .url("$url/addCalories")
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
    fun editCaloriesType(token: String, id: Int, type: String, param: (Any) -> Unit):Int {
        var resStatus=-1
        val body = FormBody.Builder()
            .add("id", id.toString())
            .add("type", type)
            .build()

        val request = Request.Builder()
            .url("$url/editCaloriesType")
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
        } catch (e: IOException) {
            Log.e("deleteCalories", "Failed to execute network request", e)
            // 处理网络请求失败的情况，例如弹出一个Toast提示用户网络连接不可用
        }
        return resStatus
    }

    fun editCaloriesTitle(token: String, id: Int, title: String, param: (Any) -> Unit):Int {
        var resStatus=-1
        val body = FormBody.Builder()
            .add("id", id.toString())
            .add("title", title)
            .build()

        val request = Request.Builder()
            .url("$url/editCaloriesTitle")
            .addHeader("Authorization",token)
            .post(body)
            .build()

        try {
            client.newCall(request).execute().use { response ->
                val gson = Gson()
                val parsed: BaseParse = gson.fromJson(response.body!!.string(), BaseParse::class.java)
                resStatus = parsed.status?:-1
                response.close()
            }
        } catch (e: IOException) {
            Log.e("deleteCalories", "Failed to execute network request", e)
            // 处理网络请求失败的情况，例如弹出一个Toast提示用户网络连接不可用
        }
        return resStatus


        return resStatus
    }

    fun editCaloriesContent(token: String, id: Int, content: String, param: (Any) -> Unit):Int {
        var resStatus=-1
        val body = FormBody.Builder()
            .add("id", id.toString())
            .add("content", content)
            .build()

        val request = Request.Builder()
            .url("$url/editCaloriesContent")
            .addHeader("Authorization",token)
            .post(body)
            .build()

        try {
            client.newCall(request).execute().use { response ->
                val gson = Gson()
                val parsed: BaseParse = gson.fromJson(response.body!!.string(), BaseParse::class.java)
                resStatus = parsed.status?:-1
                response.close()
            }
        } catch (e: IOException) {
            Log.e("deleteCalories", "Failed to execute network request", e)
            // 处理网络请求失败的情况，例如弹出一个Toast提示用户网络连接不可用
        }
        return resStatus
    }

    fun editCaloriesCalories(token: String, id: Int, calories: String, param: (Any) -> Unit):Int {
        var resStatus=-1
        val body = FormBody.Builder()
            .add("id", id.toString())
            .add("calories", calories)
            .build()

        val request = Request.Builder()
            .url("$url/editCaloriesCalories")
            .addHeader("Authorization",token)
            .post(body)
            .build()

        try {
            client.newCall(request).execute().use { response ->
                val gson = Gson()
                val parsed: BaseParse = gson.fromJson(response.body!!.string(), BaseParse::class.java)
                resStatus = parsed.status?:-1
                response.close()
            }
        } catch (e: IOException) {
            Log.e("deleteCalories", "Failed to execute network request", e)
            // 处理网络请求失败的情况，例如弹出一个Toast提示用户网络连接不可用
        }

        return resStatus
    }

    fun editCaloriesTime(token: String, id: Int, time: String, param: (Any) -> Unit):Int {
        var resStatus=-1
        val body = FormBody.Builder()
            .add("id", id.toString())
            .add("time", time) // "08:00"
            .build()

        val request = Request.Builder()
            .url("$url/editCaloriesTime")
            .addHeader("Authorization",token)
            .post(body)
            .build()

        try {
            client.newCall(request).execute().use { response ->
                val gson = Gson()
                val parsed: BaseParse =
                    gson.fromJson(response.body!!.string(), BaseParse::class.java)
                Log.d("editCaloriesTime", parsed.status.toString())
                resStatus = parsed.status ?: -1
                Log.d("测试", "parsed: ${parsed.message}")
                response.close()
            }
        }catch (e: IOException) {
            Log.e("deleteCalories", "Failed to execute network request", e)
            // 处理网络请求失败的情况，例如弹出一个Toast提示用户网络连接不可用
        }
        return resStatus
    }

    fun deleteCalories(token: String, id: Int, param: (Any) -> Unit):Int {
        var resStatus = -1
        val body = FormBody.Builder()
            .add("id", id.toString())
            .build()

        val request = Request.Builder()
            .url("$url/deleteCalories")
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
        } catch (e: IOException) {
            Log.e("deleteCalories", "Failed to execute network request", e)
            // 处理网络请求失败的情况，例如弹出一个Toast提示用户网络连接不可用
        }
        return resStatus
    }
}

