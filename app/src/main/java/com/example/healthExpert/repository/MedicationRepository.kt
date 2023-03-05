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
    fun medications(token:String, date:String): MutableList<Medication> {
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
    fun medication(token:String, id:String): Medication? {
        var medication : Medication? = null
        val request = Request.Builder()
            .url("$url/medication?id=$id")
            .addHeader("Authorization",token)
            .get()
            .build()

        client.newCall(request).execute().use { response ->
            val gson = Gson()
            val parsed: MedicationsParse = gson.fromJson(response.body!!.string(), MedicationsParse::class.java)
            if (parsed.data != null){
                medication = parsed.data!![0]
            }
            Log.d("药物", medication.toString())
            response.close()
        }
        return medication
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

    fun editMedicationName(token:String,id: String,name: String):Int {
        var resStatus=-1
        val body = FormBody.Builder()
            .add("id", id)
            .add("name", name)
            .build()

        val request = Request.Builder()
            .url("$url/editMedicationName")
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

    fun editMedicationDate(token:String,id: String,date: String):Int {
        var resStatus=-1
        val body = FormBody.Builder()
            .add("id", id)
            .add("date", date)
            .build()

        val request = Request.Builder()
            .url("$url/editMedicationDate")
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

    fun editMedicationDose(token:String,id: String,dose: String):Int {
        var resStatus=-1
        val body = FormBody.Builder()
            .add("id", id)
            .add("dose", dose)
            .build()

        val request = Request.Builder()
            .url("$url/editMedicationDose")
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

    fun editMedicationType(token:String,id: String,type: String):Int {
        var resStatus=-1
        val body = FormBody.Builder()
            .add("id", id)
            .add("type", type)
            .build()

        val request = Request.Builder()
            .url("$url/editMedicationType")
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

    fun deleteMedication(token:String,id: String):Int {
        var resStatus=-1
        val body = FormBody.Builder()
            .add("id", id.toString())
            .build()

        val request = Request.Builder()
            .url("$url/deleteMedication")
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