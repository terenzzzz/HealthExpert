package com.example.healthExpert.repository

import android.util.Log
import com.example.healthExpert.model.Calories
import com.example.healthExpert.model.Medication
import com.example.healthExpert.model.News
import com.example.healthExpert.parse.CaloriesParse
import com.example.healthExpert.parse.MedicationsParse
import com.example.healthExpert.parse.NewsParse
import com.google.gson.Gson
import okhttp3.OkHttpClient
import okhttp3.Request
import java.util.Date

class MedicationRepository {
    private val client = OkHttpClient()
    private val url = "http://terenzzzz.com:88/my"

    // 同步请求
    fun medicines(token:String, date:String): MutableList<Medication> {
        Log.d("药物1", date)
        var medications: MutableList<Medication> = mutableListOf()
        val request = Request.Builder()
            .url("$url/medications?date=$date")
            .addHeader("Authorization",token)
            .get()
            .build()

        client.newCall(request).execute().use { response ->
            val gson = Gson()
            val parsed: MedicationsParse = gson.fromJson(response.body!!.string(), MedicationsParse::class.java)
            if (parsed.data != null){
                for (medicationsInfo in parsed.data!!){
                    medications.add(medicationsInfo)
                }
            }
            Log.d("药物", medications.toString())
            response.close()
        }
        return medications
    }
}