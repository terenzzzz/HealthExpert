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

class CaloriesRepository {
    private val client = OkHttpClient()
    private val url = "http://terenzzzz.com:88/my"


    // 同步请求
    fun getCaloriesOverall(token:String,date:String): CaloriesOverall? {
        var caloriesOverall: CaloriesOverall? = null
        val request = Request.Builder()
            .url("$url/caloriesOverall?date=$date")
            .addHeader("Authorization",token)
            .get()
            .build()

        try {
            client.newCall(request).execute().use { response ->
                val gson = Gson()
                val parsed: CaloriesOverallParse = gson.fromJson(response.body!!.string(), CaloriesOverallParse::class.java)
                Log.w("getCaloriesOverall", "getCaloriesOverall调用1")
                if (parsed.data != null){
                    caloriesOverall = parsed.data!!
                }
                response.close()
            }
        } catch (e:IOException){
            caloriesOverall = null
        }
        return caloriesOverall
    }

    // 异步请求
    fun updateCaloriesOverall(token:String):Int{
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
    fun getCalories(token:String,date: String): MutableList<Calories>? {
        var calories: MutableList<Calories>? = mutableListOf()
        val request = Request.Builder()
            .url("$url/calories?date=$date")
            .addHeader("Authorization",token)
            .get()
            .build()

        try{
            client.newCall(request).execute().use { response ->
                val gson = Gson()
                val parsed: CaloriesParse = gson.fromJson(response.body!!.string(), CaloriesParse::class.java)
                Log.w("CaloriesRepository", "message: " + parsed.message)
                if (parsed.data != null){
                    for (caloriesInfo in parsed.data!!){
                        calories!!.add(caloriesInfo)
                    }
                }
                response.close()
            }
        }catch (e:IOException){
            calories = null
        }

        return calories
    }

    // 同步请求
    fun getCaloriesInfo(token:String,id:Int): Calories? {
        var calories = Calories()
        val request = Request.Builder()
            .url("$url/caloriesInfo?id=$id")
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
            .url("$url/editCaloriesType")
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
            .url("$url/editCaloriesTitle")
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
            .url("$url/editCaloriesContent")
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
            .url("$url/editCaloriesCalories")
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
            .url("$url/editCaloriesTime")
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
            .url("$url/deleteCalories")
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