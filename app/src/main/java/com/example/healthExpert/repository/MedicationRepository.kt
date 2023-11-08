package com.example.healthExpert.repository

import android.util.Log
import com.example.healthExpert.model.Calories
import com.example.healthExpert.model.Medication
import com.example.healthExpert.model.News
import com.example.healthExpert.parse.*
import com.google.gson.Gson
import okhttp3.*
import java.io.IOException
import java.util.Date

class MedicationRepository {
    private val client = OkHttpClient()
    private val url = "http://terenzzzz.cn:88/my"

    // 同步请求
    fun medications(token:String, date:String): MedicationsParse {
        var parsed = MedicationsParse()
        val request = Request.Builder()
            .url("$url/medications?date=$date")
            .addHeader("Authorization",token)
            .get()
            .build()
        try {
            client.newCall(request).execute().use { response ->
                val gson = Gson()
                parsed = gson.fromJson(response.body!!.string(), MedicationsParse::class.java)
                response.close()
            }
        }catch (e: IOException){ }
        return parsed
    }

    // 同步请求
    fun medication(token:String, id:String): MedicationsParse? {
        var parsed  = MedicationsParse()
        val request = Request.Builder()
            .url("$url/medication?id=$id")
            .addHeader("Authorization",token)
            .get()
            .build()
        try {
            client.newCall(request).execute().use { response ->
                val gson = Gson()
                parsed = gson.fromJson(response.body!!.string(), MedicationsParse::class.java)
                response.close()
            }
        }catch (e: IOException){
        }
        return parsed
    }

    // 同步请求
    fun pendingMedications(token:String): MedicationsParse {
        var parsed = MedicationsParse()
        val request = Request.Builder()
            .url("$url/pendingMedications")
            .addHeader("Authorization",token)
            .get()
            .build()
        try {
            client.newCall(request).execute().use { response ->
                val gson = Gson()
                parsed = gson.fromJson(response.body!!.string(), MedicationsParse::class.java)
                response.close()
            }
        }catch (e: IOException){ }
        return parsed
    }


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

        try {
            client.newCall(request).execute().use { response ->
                val gson = Gson()
                val parsed: BaseParse = gson.fromJson(response.body!!.string(), BaseParse::class.java)
                Log.d("editCaloriesType", parsed.status.toString())
                resStatus = parsed.status?:-1
                response.close()
            }
        }catch (e:IOException){}
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

        try {
            client.newCall(request).execute().use { response ->
                val gson = Gson()
                val parsed: BaseParse = gson.fromJson(response.body!!.string(), BaseParse::class.java)
                Log.d("editCaloriesType", parsed.status.toString())
                resStatus = parsed.status?:-1
                response.close()
            }
        }catch (e:IOException){}

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

        try {
            client.newCall(request).execute().use { response ->
                val gson = Gson()
                val parsed: BaseParse = gson.fromJson(response.body!!.string(), BaseParse::class.java)
                Log.d("editCaloriesType", parsed.status.toString())
                resStatus = parsed.status?:-1
                response.close()
            }
        }catch (e:IOException){}


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

        try {
            client.newCall(request).execute().use { response ->
                val gson = Gson()
                val parsed: BaseParse = gson.fromJson(response.body!!.string(), BaseParse::class.java)
                Log.d("editCaloriesType", parsed.status.toString())
                resStatus = parsed.status?:-1
                response.close()
            }
        }catch (e:IOException){}

        return resStatus
    }

    fun editMedicationStatus(token:String,id: String,status: String):Int {
        var resStatus=-1
        val body = FormBody.Builder()
            .add("id", id)
            .add("status", status)
            .build()

        val request = Request.Builder()
            .url("$url/editMedicationStatus")
            .addHeader("Authorization",token)
            .post(body)
            .build()

        try {
            client.newCall(request).execute().use { response ->
                val gson = Gson()
                val parsed: BaseParse = gson.fromJson(response.body!!.string(), BaseParse::class.java)
                Log.d("editMedicationStatus", parsed.status.toString())
                resStatus = parsed.status
                response.close()
            }
        }catch (e:IOException){}

        return resStatus
    }

    fun deleteMedication(token:String,id: String):Int {
        var resStatus=-1
        val body = FormBody.Builder()
            .add("id", id)
            .build()

        val request = Request.Builder()
            .url("$url/deleteMedication")
            .addHeader("Authorization",token)
            .post(body)
            .build()
        try {
            client.newCall(request).execute().use { response ->
                val gson = Gson()
                val parsed: BaseParse = gson.fromJson(response.body!!.string(), BaseParse::class.java)
                Log.d("deleteCalories", parsed.status.toString())
                resStatus = parsed.status?:-1
                response.close()
            }
        }catch (e:IOException){}
        return resStatus
    }
}