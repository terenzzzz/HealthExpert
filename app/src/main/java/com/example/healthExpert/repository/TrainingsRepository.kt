package com.example.healthExpert.repository

import android.util.Log
import com.example.healthExpert.model.*
import com.example.healthExpert.parse.*
import com.google.gson.Gson
import okhttp3.*
import java.io.IOException

class TrainingsRepository {
    private val client = OkHttpClient()

    // 同步请求
    fun getTrainingOverall(token:String): TrainingOverall {
        var trainingOverall = TrainingOverall()
        val request = Request.Builder()
            .url("http://terenzzzz.com:88/my/trainingOverall")
            .addHeader("Authorization",token)
            .get()
            .build()

        client.newCall(request).execute().use { response ->
            val gson = Gson()
            val parsed: TrainingOverallParse = gson.fromJson(response.body!!.string(), TrainingOverallParse::class.java)
            Log.w("TrainingsRepository", "trainingOverall调用")
            if (parsed.data != null){
                trainingOverall = parsed.data!!
            }
            response.close()
        }
        return trainingOverall
    }

    // 异步请求
    fun updateWaterOverall(token:String):Int{
        var resStatus=-1
        val body = FormBody.Builder().build()

        val request = Request.Builder()
            .url("http://terenzzzz.com:88/my/updateTrainingOverall")
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
                    Log.w("TrainingsRepository", "更新TrainingOverall")
                    response.close()
                }
            }
        })
        return resStatus
    }

    // 同步请求
    fun getTrainings(token:String): MutableList<Trainings> {
        var trainings: MutableList<Trainings> = mutableListOf()
        val request = Request.Builder()
            .url("http://terenzzzz.com:88/my/trainings")
            .addHeader("Authorization",token)
            .get()
            .build()

        client.newCall(request).execute().use { response ->
            val gson = Gson()
            val parsed: TrainingsParse = gson.fromJson(response.body!!.string(), TrainingsParse::class.java)
            Log.w("TrainingRepository", "message: " + parsed.message)
            if (parsed.data != null){
                for (training in parsed.data!!){
                    trainings.add(training)
                }
            }
            response.close()
        }
        return trainings
    }

    // 同步请求
    fun getTrainingInfo(token:String,id:Int): MutableList<Trainings> {
        var trainings: MutableList<Trainings> = mutableListOf()
        val request = Request.Builder()
            .url("http://terenzzzz.com:88/my/trainingInfo?id=$id")
            .addHeader("Authorization",token)
            .get()
            .build()

        client.newCall(request).execute().use { response ->
            val gson = Gson()
            val parsed: TrainingsParse = gson.fromJson(response.body!!.string(), TrainingsParse::class.java)
            Log.w("TrainingRepository", "getTrainingInfo: " + parsed.message)
            if (parsed.data != null){
                for (training in parsed.data!!){
                    trainings.add(training)
                }
            }
            Log.d("getTrainingInfo", trainings.toString())
            response.close()
        }
        return trainings
    }

    // 同步请求
    fun getTrainingLocations(token:String,idTraining:Int): MutableList<Location> {
        var locations: MutableList<Location> = mutableListOf()
        val request = Request.Builder()
            .url("http://terenzzzz.com:88/my/trainingLocation?idTraining=$idTraining")
            .addHeader("Authorization",token)
            .get()
            .build()

        client.newCall(request).execute().use { response ->
            val gson = Gson()
            val parsed: LocationParse = gson.fromJson(response.body!!.string(), LocationParse::class.java)
            if (parsed.data != null){
                for (training in parsed.data!!){
                    var newLocation = Location(training.latitude!!,training.longitude!!)
                    locations.add(newLocation)
                }
            }
            Log.d("getTrainingLocations", locations.toString())

            response.close()
        }
        return locations
    }



    // 同步请求
    fun addTraining(token:String,
                    type:String,
                    title:String,
                    distance:String,
                    speed:String,
                    caloriesBurn:String,
                    startTime:String,
                    endTime:String,
                    ): Int {
        var insertId=-1
        val body = FormBody.Builder()
            .add("type", type)
            .add("title", title)
            .add("distance", distance)
            .add("speed", speed)
            .add("caloriesBurn", caloriesBurn)
            .add("startTime", startTime)
            .add("endTime", endTime)
            .build()
        val request = Request.Builder()
            .url("http://terenzzzz.com:88/my/addTraining")
            .addHeader("Authorization",token)
            .post(body)
            .build()

        client.newCall(request).execute().use { response ->
            val gson = Gson()
            val parsed: BaseParse = gson.fromJson(response.body!!.string(), BaseParse::class.java)
            Log.w("addLocations", "message: ${parsed.message}")
            insertId = parsed.insertId?:-1
            Log.w("addLocations", "insertId: $insertId")
            response.close()
        }
        return insertId
    }

    // 同步请求
    fun addLocations(token:String,insertId: Int, locations:String): Int {
        var resStatus=-1
        val body = FormBody.Builder()
            .add("insertId", insertId.toString())
            .add("locations", locations)
            .build()
        val request = Request.Builder()
            .url("http://terenzzzz.com:88/my/addLocations")
            .addHeader("Authorization",token)
            .post(body)
            .build()

        client.newCall(request).execute().use { response ->
            val gson = Gson()
            val parsed: BaseParse = gson.fromJson(response.body!!.string(), BaseParse::class.java)
            resStatus = parsed.status?:-1
            Log.w("addLocations", "message: " + parsed.message)
            response.close()
        }
        return resStatus
    }


    fun deleteTraining(token:String,id: Int): Int {
        var resStatus=-1
        val body = FormBody.Builder()
            .add("id", id.toString())
            .build()
        val request = Request.Builder()
            .url("http://terenzzzz.com:88/my/deleteTraining")
            .addHeader("Authorization",token)
            .post(body)
            .build()

        client.newCall(request).execute().use { response ->
            val gson = Gson()
            val parsed: BaseParse = gson.fromJson(response.body!!.string(), BaseParse::class.java)
            resStatus = parsed.status?:-1
            Log.w("deleteTraining", "message: " + parsed.message)
            response.close()
        }
        return resStatus
    }

    fun deleteTrainingLocation(token:String,idTraining: Int): Int {
        var resStatus=-1
        val body = FormBody.Builder()
            .add("idTraining", idTraining.toString())
            .build()
        val request = Request.Builder()
            .url("http://terenzzzz.com:88/my/deleteTrainingLocation")
            .addHeader("Authorization",token)
            .post(body)
            .build()

        client.newCall(request).execute().use { response ->
            val gson = Gson()
            val parsed: BaseParse = gson.fromJson(response.body!!.string(), BaseParse::class.java)
            resStatus = parsed.status?:-1
            Log.w("deleteTrainingLocation", "message: " + parsed.message)
            response.close()
        }
        return resStatus
    }


}