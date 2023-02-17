package com.example.healthExpert.repository

import android.util.Log
import com.example.healthExpert.model.Calories
import com.example.healthExpert.model.CaloriesOverall
import com.example.healthExpert.parse.BaseParse
import com.example.healthExpert.parse.CaloriesOverallParse
import com.example.healthExpert.parse.CaloriesParse
import com.google.gson.Gson
import okhttp3.*
import java.io.IOException
import kotlin.math.log

class CaloriesRepository {
    private val client = OkHttpClient()

    // 同步请求
    fun getCaloriesOverall(token:String): CaloriesOverall {
        var caloriesOverall = CaloriesOverall()
        val request = Request.Builder()
            .url("http://terenzzzz.com:88/my/caloriesOverall")
            .addHeader("Authorization",token)
            .get()
            .build()

        client.newCall(request).execute().use { response ->
//            Log.d("getCaloriesOverall", response.toString())
            val gson = Gson()
            val parsed: CaloriesOverallParse = gson.fromJson(response.body!!.string(), CaloriesOverallParse::class.java)
            Log.w("getCaloriesOverall", "message: " + parsed.message)
            if (parsed.data != null){
                caloriesOverall = parsed.data!!
            }
            response.close()
        }
        return caloriesOverall
    }

    // 异步请求
    fun addCaloriesOverall(token:String,intake:String,burn:String):Int{
        var resStatus=-1
        val body = FormBody.Builder()
            .add("intake", intake)
            .add("burn", burn)
            .build()

        val request = Request.Builder()
            .url("http://terenzzzz.com:88/my/addCaloriesOverall")
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
                    Log.w("CaloriesRepository", "addCaloriesOverall: $resStatus")
                    response.close()
                }
            }
        })
        return resStatus
    }

    // 异步请求
    fun subCaloriesOverall(token:String,intake:String,burn:String):Int{
        var resStatus=-1
        val body = FormBody.Builder()
            .add("intake", intake)
            .add("burn", burn)
            .build()

        val request = Request.Builder()
            .url("http://terenzzzz.com:88/my/subCaloriesOverall")
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
                    Log.w("CaloriesRepository", "subCaloriesOverall: $resStatus")
                    response.close()
                }
            }
        })
        return resStatus
    }



    // 同步请求
    fun getCalories(token:String): MutableList<Calories> {
        var calories: MutableList<Calories> = mutableListOf()
        val request = Request.Builder()
            .url("http://terenzzzz.com:88/my/calories")
            .addHeader("Authorization",token)
            .get()
            .build()

        client.newCall(request).execute().use { response ->
            val gson = Gson()
            val parsed: CaloriesParse = gson.fromJson(response.body!!.string(), CaloriesParse::class.java)
            Log.w("CaloriesRepository", "message: " + parsed.message)
            if (parsed.data != null){
                for (caloriesInfo in parsed.data!!){
                    calories.add(caloriesInfo)
                }
            }
            response.close()
        }
        return calories
    }

    // 同步请求
    fun getCaloriesInfo(token:String,id:Int): Calories? {
        var calories = Calories()
        val request = Request.Builder()
            .url("http://terenzzzz.com:88/my/caloriesInfo?id=$id")
            .addHeader("Authorization",token)
            .get()
            .build()

        client.newCall(request).execute().use { response ->
            val gson = Gson()
            val parsed: CaloriesParse = gson.fromJson(response.body!!.string(), CaloriesParse::class.java)
            Log.w("getCaloriesInfo", "message: " + parsed.message)
            if (parsed.data != null){
                calories = parsed.data!![0]
            }
            response.close()
        }
        return calories
    }

    // 异步请求
    fun addCalories(token:String,type:String,title:String,content:String,calories:Int,time:String):Int{
        var resStatus=-1
        val body = FormBody.Builder()
            .add("type", type)
            .add("title", title)
            .add("content", content)
            .add("calories", calories.toString())
            .add("time", time)
            .build()

        val request = Request.Builder()
            .url("http://terenzzzz.com:88/my/addCalories")
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
                    Log.w("CaloriesRepository", "addCalories: $resStatus")
                    response.close()
                }
            }
        })
        return resStatus
    }

    fun editCaloriesType(token:String,id: Int,type: String):Int {
        var resStatus=-1
        val body = FormBody.Builder()
            .add("id", id.toString())
            .add("type", type)
            .build()

        val request = Request.Builder()
            .url("http://terenzzzz.com:88/my/editCaloriesType")
            .addHeader("Authorization",token)
            .post(body)
            .build()

        client.newCall(request).execute().use { response ->
            val gson = Gson()
            val parsed: BaseParse = gson.fromJson(response.body!!.string(), BaseParse::class.java)
            Log.d("editCaloriesType", parsed.status.toString())
            resStatus = parsed.status?:-1
            response.close()
        }
        return resStatus
    }

    fun editCaloriesTitle(token:String,id: Int,title: String):Int {
        var resStatus=-1
        val body = FormBody.Builder()
            .add("id", id.toString())
            .add("title", title)
            .build()

        val request = Request.Builder()
            .url("http://terenzzzz.com:88/my/editCaloriesTitle")
            .addHeader("Authorization",token)
            .post(body)
            .build()

        client.newCall(request).execute().use { response ->
            val gson = Gson()
            val parsed: BaseParse = gson.fromJson(response.body!!.string(), BaseParse::class.java)
            Log.d("editCaloriesTitle", parsed.status.toString())
            resStatus = parsed.status?:-1
            response.close()
        }
        return resStatus
    }

    fun editCaloriesContent(token:String,id: Int,content: String):Int {
        var resStatus=-1
        val body = FormBody.Builder()
            .add("id", id.toString())
            .add("content", content)
            .build()

        val request = Request.Builder()
            .url("http://terenzzzz.com:88/my/editCaloriesContent")
            .addHeader("Authorization",token)
            .post(body)
            .build()

        client.newCall(request).execute().use { response ->
            val gson = Gson()
            val parsed: BaseParse = gson.fromJson(response.body!!.string(), BaseParse::class.java)
            Log.d("editCaloriesContent", parsed.status.toString())
            resStatus = parsed.status?:-1
            response.close()
        }
        return resStatus
    }

    fun editCaloriesCalories(token:String,id: Int,calories: String):Int {
        var resStatus=-1
        val body = FormBody.Builder()
            .add("id", id.toString())
            .add("calories", calories)
            .build()

        val request = Request.Builder()
            .url("http://terenzzzz.com:88/my/editCaloriesCalories")
            .addHeader("Authorization",token)
            .post(body)
            .build()

        client.newCall(request).execute().use { response ->
            val gson = Gson()
            val parsed: BaseParse = gson.fromJson(response.body!!.string(), BaseParse::class.java)
            Log.d("editCaloriesCalories", parsed.status.toString())
            resStatus = parsed.status?:-1
            response.close()
        }
        return resStatus
    }

    fun editCaloriesTime(token:String,id: Int,time: String):Int {
        var resStatus=-1
        val body = FormBody.Builder()
            .add("id", id.toString())
            .add("time", time) // "08:00"
            .build()

        val request = Request.Builder()
            .url("http://terenzzzz.com:88/my/editCaloriesTime")
            .addHeader("Authorization",token)
            .post(body)
            .build()

        client.newCall(request).execute().use { response ->
            val gson = Gson()
            val parsed: BaseParse = gson.fromJson(response.body!!.string(), BaseParse::class.java)
            Log.d("editCaloriesTime", parsed.status.toString())
            resStatus = parsed.status?:-1
            response.close()
        }
        return resStatus
    }

    fun deleteCalories(token:String,id: Int):Int {
        var resStatus=-1
        val body = FormBody.Builder()
            .add("id", id.toString())
            .build()

        val request = Request.Builder()
            .url("http://terenzzzz.com:88/my/deleteCalories")
            .addHeader("Authorization",token)
            .post(body)
            .build()

        client.newCall(request).execute().use { response ->
            val gson = Gson()
            val parsed: BaseParse = gson.fromJson(response.body!!.string(), BaseParse::class.java)
            Log.d("deleteCalories", parsed.status.toString())
            resStatus = parsed.status?:-1
            response.close()
        }
        return resStatus
    }
}