package com.example.healthExpert.utils

import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

class DateTimeConvert {

    fun toDate(datetime:Date): String {
        return SimpleDateFormat("yyyy-MM-dd").format(datetime)
    }

    fun toTime(datetime:Date): String {
        return SimpleDateFormat("HH:mm:ss").format(datetime)
    }

    fun toDateTime(datetime:Date): String {
        return SimpleDateFormat("YYYY-mm-dd HH:mm:ss").format(datetime)
    }

    fun toHHmm(datetime:Date): String {
        return SimpleDateFormat("HH:mm").format(datetime)
    }

    fun subTimes(startTime: String, endTime: String): String {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        val startDateTime = dateFormat.parse(startTime)
        val endDateTime = dateFormat.parse(endTime)
        val timeDifferenceMillis = endDateTime.time - startDateTime.time
        return String.format(
            "%02d:%02d:%02d",
            TimeUnit.MILLISECONDS.toHours(timeDifferenceMillis),
            TimeUnit.MILLISECONDS.toMinutes(timeDifferenceMillis) % TimeUnit.HOURS.toMinutes(1),
            TimeUnit.MILLISECONDS.toSeconds(timeDifferenceMillis) % TimeUnit.MINUTES.toSeconds(1)
        )
    }

}