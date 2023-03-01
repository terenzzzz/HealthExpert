package com.example.healthExpert.repository

import android.util.Log
import com.example.healthExpert.model.Calories
import com.example.healthExpert.model.Medication
import com.example.healthExpert.model.News
import com.example.healthExpert.parse.BaseParse
import com.example.healthExpert.parse.CaloriesParse
import com.example.healthExpert.parse.MedicationsParse
import com.example.healthExpert.parse.NewsParse
import com.google.gson.Gson
import okhttp3.*
import java.io.IOException
import java.util.Date

class MedicationRepository {
    private val client = OkHttpClient()
    private val url = "http://terenzzzz.com:88/my"

    // 同步请求
    fun medicines(token:String, date:String): MutableList<Medication> {
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

    // 同步请求
    fun pendingMedications(token:String): MutableList<Medication> {
        var medications: MutableList<Medication> = mutableListOf()
        val request = Request.Builder()
            .url("$url/pendingMedications")
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

    // 异步请求
    fun addMedication(token:String,type:String,name:String,dose:String,date:String):Int{
        var resStatus=-1
        val body = FormBody.Builder()
            .add("type", type)
            .add("name", name)
            .add("dose", dose)
            .add("date", date)
            .build()

        val request = Request.Builder()
            .url("$url/addMedication")
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
                    Log.w("MedicationRepository", "addMedication: $resStatus")
                    response.close()
                }
            }
        })
        return resStatus
    }
}