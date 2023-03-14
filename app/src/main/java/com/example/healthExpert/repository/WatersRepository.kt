package com.example.healthExpert.repository

import android.util.Log
import com.example.healthExpert.model.Calories
import com.example.healthExpert.model.CaloriesOverall
import com.example.healthExpert.model.Water
import com.example.healthExpert.model.WaterOverall
import com.example.healthExpert.parse.*
import com.google.gson.Gson
import okhttp3.*
import java.io.IOException

class WatersRepository {
    private val client = OkHttpClient()
    private val url = "http://terenzzzz.com:88/my"

    // 同步请求
    fun getWaterOverall(token:String,date:String): WaterOverall? {
        var waterOverall: WaterOverall? = null
        val request = Request.Builder()
            .url("$url/waterOverall?date=$date")
            .addHeader("Authorization",token)
            .get()
            .build()
        try {
            client.newCall(request).execute().use { response ->
                val gson = Gson()
                val parsed: WaterOverallParse = gson.fromJson(response.body!!.string(), WaterOverallParse::class.java)
                Log.w("WatersRepository", "getWaterOverall调用1")
                if (parsed.data != null){
                    waterOverall = parsed.data!!
                }
                response.close()
            }
        }catch (e: IOException){
            waterOverall = null
        }
        return waterOverall
    }

    // 异步请求
    fun updateWaterOverall(token:String):Int{
        var resStatus=-1
        val body = FormBody.Builder().build()

        val request = Request.Builder()
            .url("$url/updateWaterOverall")
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
                    Log.w("WatersRepository", "更新WaterOverall")
                    response.close()
                }
            }
        })
        return resStatus
    }

    // 同步请求
    fun getWaters(token: String): MutableList<Water> {
        var water: MutableList<Water> = mutableListOf()
        val request = Request.Builder()
            .url("$url/waters")
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

    // 同步请求
    fun getWatersInfo(token:String,id:Int): MutableList<Water> {
        var water: MutableList<Water> = mutableListOf()
        val request = Request.Builder()
            .url("$url/watersInfo?id=$id")
            .addHeader("Authorization",token)
            .get()
            .build()

        client.newCall(request).execute().use { response ->
            val gson = Gson()
            val parsed: WaterParse = gson.fromJson(response.body!!.string(), WaterParse::class.java)
            Log.w("getWatersInfo", "message: " + parsed.message)
            if (parsed.data != null){
                for (training in parsed.data!!){
                    water.add(training)
                }
            }
            response.close()
        }
        return water
    }

    // 异步请求
    fun addWaters(token:String,type:String,title:String,content:String,value:Int,time:String):Int{
        var resStatus=-1
        val body = FormBody.Builder()
            .add("type", type)
            .add("title", title)
            .add("content", content)
            .add("value", value.toString())
            .add("time", time)
            .build()

        val request = Request.Builder()
            .url("$url/addWaters")
            .addHeader("Authorization",token)
            .post(body)
            .build()

        client.newCall(request).execute().use { response ->
            val gson = Gson()
            val parsed: BaseParse = gson.fromJson(response.body!!.string(), BaseParse::class.java)
            resStatus = parsed.status?:-1
            Log.w("addCalories", "addCalories: $resStatus")
            response.close()
        }
        return resStatus
    }

    fun editWatersType(token:String,id: Int,type: String):Int {
        var resStatus=-1
        val body = FormBody.Builder()
            .add("id", id.toString())
            .add("type", type)
            .build()

        val request = Request.Builder()
            .url("$url/editWatersType")
            .addHeader("Authorization",token)
            .post(body)
            .build()

        client.newCall(request).execute().use { response ->
            val gson = Gson()
            val parsed: BaseParse = gson.fromJson(response.body!!.string(), BaseParse::class.java)
            Log.d("editWatersType", parsed.status.toString())
            resStatus = parsed.status?:-1
            response.close()
        }
        return resStatus
    }

    fun editWatersTitle(token:String,id: Int,title: String):Int {
        var resStatus=-1
        val body = FormBody.Builder()
            .add("id", id.toString())
            .add("title", title)
            .build()

        val request = Request.Builder()
            .url("$url/editWatersTitle")
            .addHeader("Authorization",token)
            .post(body)
            .build()

        client.newCall(request).execute().use { response ->
            val gson = Gson()
            val parsed: BaseParse = gson.fromJson(response.body!!.string(), BaseParse::class.java)
            Log.d("editWatersTitle", parsed.status.toString())
            resStatus = parsed.status?:-1
            response.close()
        }
        return resStatus
    }

    fun editWatersContent(token:String,id: Int,content: String):Int {
        var resStatus=-1
        val body = FormBody.Builder()
            .add("id", id.toString())
            .add("content", content)
            .build()

        val request = Request.Builder()
            .url("$url/editWatersContent")
            .addHeader("Authorization",token)
            .post(body)
            .build()

        client.newCall(request).execute().use { response ->
            val gson = Gson()
            val parsed: BaseParse = gson.fromJson(response.body!!.string(), BaseParse::class.java)
            Log.d("editWatersContent", parsed.status.toString())
            resStatus = parsed.status?:-1
            response.close()
        }
        return resStatus
    }

    fun editWatersValue(token:String,id: Int,value: String):Int {
        var resStatus=-1
        val body = FormBody.Builder()
            .add("id", id.toString())
            .add("value", value)
            .build()

        val request = Request.Builder()
            .url("$url/editWatersValue")
            .addHeader("Authorization",token)
            .post(body)
            .build()

        client.newCall(request).execute().use { response ->
            val gson = Gson()
            val parsed: BaseParse = gson.fromJson(response.body!!.string(), BaseParse::class.java)
            Log.d("editWatersValue", parsed.status.toString())
            resStatus = parsed.status?:-1
            response.close()
        }
        return resStatus
    }

    fun editWatersTime(token:String,id: Int,time: String):Int {
        var resStatus=-1
        val body = FormBody.Builder()
            .add("id", id.toString())
            .add("time", time) // "08:00"
            .build()

        val request = Request.Builder()
            .url("$url/editWatersTime")
            .addHeader("Authorization",token)
            .post(body)
            .build()

        client.newCall(request).execute().use { response ->
            val gson = Gson()
            val parsed: BaseParse = gson.fromJson(response.body!!.string(), BaseParse::class.java)
            Log.d("editWatersTime", parsed.status.toString())
            resStatus = parsed.status?:-1
            response.close()
        }
        return resStatus
    }

    fun deleteWaters(token:String,id: Int):Int {
        var resStatus=-1
        val body = FormBody.Builder()
            .add("id", id.toString())
            .build()

        val request = Request.Builder()
            .url("$url/deleteWaters")
            .addHeader("Authorization",token)
            .post(body)
            .build()

        client.newCall(request).execute().use { response ->
            val gson = Gson()
            val parsed: BaseParse = gson.fromJson(response.body!!.string(), BaseParse::class.java)
            Log.d("deleteWaters", parsed.status.toString())
            resStatus = parsed.status?:-1
            response.close()
        }
        return resStatus
    }
}