package com.example.healthExpert.utils

import java.text.SimpleDateFormat
import java.util.Date

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

}