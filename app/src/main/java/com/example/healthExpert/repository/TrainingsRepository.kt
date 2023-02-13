package com.example.healthExpert.repository

import android.util.Log
import com.example.healthExpert.model.Calories
import com.example.healthExpert.model.Trainings
import com.example.healthExpert.parse.CaloriesParse
import com.example.healthExpert.parse.TrainingsParse
import com.google.gson.Gson
import okhttp3.OkHttpClient
import okhttp3.Request

class TrainingsRepository {
    private val client = OkHttpClient()

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
}