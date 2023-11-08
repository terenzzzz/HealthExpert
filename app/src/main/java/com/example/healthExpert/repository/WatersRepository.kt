package com.example.healthExpert.repository

import android.util.Log
import com.example.healthExpert.parse.*
import com.google.gson.Gson
import okhttp3.*
import java.io.IOException

class WatersRepository {
    private val client = OkHttpClient()
    private val url = "http://terenzzzz.cn:88/my"

    // 同步请求
    fun getWaterOverall(token:String,date:String): WaterOverallParse {
        var parsed = WaterOverallParse()
        val request = Request.Builder()
            .url("$url/waterOverall?date=$date")
            .addHeader("Authorization",token)
            .get()
            .build()
        try {
            client.newCall(request).execute().use { response ->
                val gson = Gson()
                parsed = gson.fromJson(response.body!!.string(), WaterOverallParse::class.java)
                response.close()
            }
        }catch (e: IOException){}
        return parsed
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
                    val parsed: BaseParse =
                        gson.fromJson(response.body!!.string(), BaseParse::class.java)
                    resStatus = parsed.status ?: -1
                    Log.w("WatersRepository", "更新WaterOverall")
                    response.close()
                }
            }
        })
        return resStatus
    }

    // 同步请求
    fun getWaters(token: String,date: String): WaterParse {
        var parsed = WaterParse()
        val request = Request.Builder()
            .url("$url/waters?date=$date")
            .addHeader("Authorization",token)
            .get()
            .build()

        try {
            client.newCall(request).execute().use { response ->
                val gson = Gson()
                parsed = gson.fromJson(response.body!!.string(), WaterParse::class.java)
                response.close()
            }
        }catch (e: IOException) { }
        return parsed
    }

    // 同步请求
    fun getWatersInfo(token:String,id:Int): WaterParse {
        var parsed = WaterParse()
        val request = Request.Builder()
            .url("$url/watersInfo?id=$id")
            .addHeader("Authorization",token)
            .get()
            .build()

        try {
            client.newCall(request).execute().use { response ->
                val gson = Gson()
                parsed = gson.fromJson(response.body!!.string(), WaterParse::class.java)
                response.close()
            }
        }catch (e: IOException) {

        }
        return parsed
    }

    // 异步请求
    fun addWaters(
        token: String,
        type: String,
        title: String,
        content: String,
        value: Int,
        time: String,
        param: (Any) -> Unit
    ):Int{
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
        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                resStatus = -1
            }

            override fun onResponse(call: Call, response: Response) {
                val gson = Gson()
                val parsed: BaseParse = gson.fromJson(response.body!!.string(), BaseParse::class.java)
                resStatus = parsed.status?:-1
                response.close()
            }
        })
        return resStatus
    }

    fun editWatersType(token: String, id: Int, type: String, param: (Any) -> Unit):Int {
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

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                resStatus = -1
            }

            override fun onResponse(call: Call, response: Response) {
                val gson = Gson()
                val parsed: BaseParse = gson.fromJson(response.body!!.string(), BaseParse::class.java)
                Log.d("editWatersType", parsed.status.toString())
                resStatus = parsed.status?:-1
                response.close()
            }
        })

        return resStatus
    }

    fun editWatersTitle(token: String, id: Int, title: String, param: (Any) -> Unit):Int {
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

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                resStatus = -1
            }

            override fun onResponse(call: Call, response: Response) {
                val gson = Gson()
                val parsed: BaseParse = gson.fromJson(response.body!!.string(), BaseParse::class.java)
                Log.d("editWatersTitle", parsed.status.toString())
                resStatus = parsed.status?:-1
                response.close()
            }
        })

        return resStatus
    }

    fun editWatersContent(token: String, id: Int, content: String, param: (Any) -> Unit):Int {
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


        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                resStatus = -1
            }

            override fun onResponse(call: Call, response: Response) {
                val gson = Gson()
                val parsed: BaseParse = gson.fromJson(response.body!!.string(), BaseParse::class.java)
                Log.d("editWatersContent", parsed.status.toString())
                resStatus = parsed.status?:-1
                response.close()
            }
        })

        return resStatus
    }

    fun editWatersValue(token: String, id: Int, value: String, param: (Any) -> Unit):Int {
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

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                resStatus = -1
            }

            override fun onResponse(call: Call, response: Response) {
                val gson = Gson()
                val parsed: BaseParse = gson.fromJson(response.body!!.string(), BaseParse::class.java)
                Log.d("editWatersValue", parsed.status.toString())
                resStatus = parsed.status?:-1
                response.close()
            }
        })
        return resStatus
    }

    fun editWatersTime(token: String, id: Int, time: String, param: (Any) -> Unit):Int {
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

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                resStatus = -1
            }

            override fun onResponse(call: Call, response: Response) {
                val gson = Gson()
                val parsed: BaseParse = gson.fromJson(response.body!!.string(), BaseParse::class.java)
                Log.d("editWatersTime", parsed.status.toString())
                resStatus = parsed.status?:-1
                response.close()
            }
        })

        return resStatus
    }

    fun deleteWaters(token: String, id: Int, param: (Any) -> Unit):Int {
        var resStatus=-1
        val body = FormBody.Builder()
            .add("id", id.toString())
            .build()

        val request = Request.Builder()
            .url("$url/deleteWaters")
            .addHeader("Authorization",token)
            .post(body)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                resStatus = -1
            }

            override fun onResponse(call: Call, response: Response) {
                val gson = Gson()
                val parsed: BaseParse = gson.fromJson(response.body!!.string(), BaseParse::class.java)
                Log.d("deleteWaters", parsed.status.toString())
                resStatus = parsed.status?:-1
                response.close()
            }
        })


        return resStatus
    }
}