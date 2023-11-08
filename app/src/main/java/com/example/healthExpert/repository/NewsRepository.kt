package com.example.healthExpert.repository

import android.util.Log
import com.example.healthExpert.model.Calories
import com.example.healthExpert.model.News
import com.example.healthExpert.parse.CaloriesParse
import com.example.healthExpert.parse.NewsParse
import com.google.gson.Gson
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.IOException

class NewsRepository {
    private val client = OkHttpClient()
    private val url = "http://terenzzzz.cn:88/api"

    // 同步请求
    fun getNews(): NewsParse {
        var parsed = NewsParse()
        val request = Request.Builder()
            .url("$url/news")
            .get()
            .build()

        try {
            client.newCall(request).execute().use { response ->
                val gson = Gson()
                parsed = gson.fromJson(response.body!!.string(), NewsParse::class.java)
                response.close()
            }
        }catch (o: IOException){
        }

        return parsed
    }

    // 同步请求
    fun getNewInfo(id:Int): MutableList<News>? {
        var news: MutableList<News>? = mutableListOf()
        val request = Request.Builder()
            .url("$url/news?id=$id")
            .get()
            .build()
        try {
            client.newCall(request).execute().use { response ->
                val gson = Gson()
                val parsed: NewsParse = gson.fromJson(response.body!!.string(), NewsParse::class.java)
                if (parsed.data != null){
                    for (newsInfo in parsed.data!!){
                        news!!.add(newsInfo)
                    }
                }
                response.close()
                Log.d("NewsRepo", news.toString())
            }
        }catch (o: IOException){
            news = null
        }
        return news
    }
}