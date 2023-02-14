package com.example.healthExpert.repository

import android.util.Log
import com.example.healthExpert.model.Calories
import com.example.healthExpert.model.News
import com.example.healthExpert.parse.CaloriesParse
import com.example.healthExpert.parse.NewsParse
import com.google.gson.Gson
import okhttp3.OkHttpClient
import okhttp3.Request

class NewsRepository {
    private val client = OkHttpClient()

    // 同步请求
    fun getNews(): MutableList<News> {
        var news: MutableList<News> = mutableListOf()
        val request = Request.Builder()
            .url("http://terenzzzz.com:88/api/news")
            .get()
            .build()

        client.newCall(request).execute().use { response ->
            val gson = Gson()
            val parsed: NewsParse = gson.fromJson(response.body!!.string(), NewsParse::class.java)
            if (parsed.data != null){
                for (newsInfo in parsed.data!!){
                    news.add(newsInfo)
                }
            }
            response.close()
        }
        return news
    }
}