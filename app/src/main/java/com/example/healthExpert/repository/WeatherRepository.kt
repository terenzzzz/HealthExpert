package com.example.healthExpert.repository

import android.os.Handler
import android.os.Looper
import android.util.Log
import com.example.healthExpert.R
import com.example.healthExpert.model.Weather
import com.example.healthExpert.parse.CaloriesParse
import com.example.healthExpert.parse.WeatherParse
import com.google.gson.Gson
import com.squareup.picasso.Picasso
import okhttp3.*
import java.io.IOException

class WeatherRepository {
    private val client = OkHttpClient()
    /**
     * An Asynchronous http Request function for weather
     *
     * @param startLocation
     * @param endLocation
     */
    fun getWeather(latitude:Double,longitude:Double): Weather {
        var weather = Weather()
        var url = "https://api.openweathermap.org/data/2.5/weather?lat=${latitude}&lon=${longitude}" +
                "&appid=bb01585d3dafe1d3b04332150c924d32&units=metric"
        val request = Request.Builder()
            .url(url)
            .build()

        client.newCall(request).execute().use { response ->
            val gson = Gson()
            val parsed: WeatherParse = gson.fromJson(response.body!!.string(), WeatherParse::class.java)
            if (parsed != null){
                weather.icon = "https://openweathermap.org/img/wn/${parsed.weather?.get(0)?.icon}@2x.png"
                weather.weather = parsed.weather?.get(0)?.main.toString()
                weather.temp = "${parsed.main?.temp_min}(℃)  -  ${parsed.main?.temp_max}(℃)"
            }
            response.close()
        }
        return weather
    }
}