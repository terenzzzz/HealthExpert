package com.example.healthExpert.utils

import android.util.Log
import java.text.SimpleDateFormat
import java.time.Duration
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

class DateTimeConvert {

    fun toDate(datetime:Date): String {
        return SimpleDateFormat("yyyy-MM-dd").format(datetime)
    }

    fun toTime(datetime:Date): String {
        return SimpleDateFormat("HH:mm:ss").format(datetime)
    }

    fun toDateTime(datetime:Date): String {
        return SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(datetime)
    }

    fun toHHmm(datetime:Date): String {
        return SimpleDateFormat("HH:mm").format(datetime)
    }

    fun subTimes(startTime: String, endTime: String): String {
        // Create formatter for the input string
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")

        // Parse the input strings into LocalDateTime objects
        val startDateTime = LocalDateTime.parse(startTime, formatter)
        val endDateTime = LocalDateTime.parse(endTime, formatter)

        // Calculate the difference between the two LocalDateTime objects
        val duration = Duration.between(startDateTime, endDateTime)


        // Extract the hours, minutes, and seconds from the duration
        val hours = duration.toHours()
        val minutes = duration.toMinutes() % 60
        val seconds = duration.seconds % 60

        // Format the result as a string with format HH:mm:ss
        return "%02d:%02d:%02d".format(hours, minutes, seconds)
    }

}