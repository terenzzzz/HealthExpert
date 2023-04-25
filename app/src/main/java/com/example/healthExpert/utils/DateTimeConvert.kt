package com.example.healthExpert.utils

import java.text.SimpleDateFormat
import java.time.Duration
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

class DateTimeConvert {
    companion object {
        fun toDate(datetime: Date): String {
            return SimpleDateFormat("yyyy-MM-dd").format(datetime)
        }

        fun toTime(datetime: Date): String {
            return SimpleDateFormat("HH:mm:ss").format(datetime)
        }

        fun toDateTime(datetime: Date): String {
            return SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(datetime)
        }

        fun toHHmm(datetime: Date): String {
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

        fun toDecimalHours(startTime: String, endTime: String): String {
            val durationString = subTimes(startTime, endTime)
            val parts = durationString.split(":")
            val hours = parts[0].toLong()
            val minutes = parts[1].toLong()
            val seconds = parts[2].toLong()
            val duration = Duration.ofHours(hours).plusMinutes(minutes).plusSeconds(seconds)
            val decimalHours = duration.toMinutes().toDouble() / 60
            return "${"%.2f".format(decimalHours)}"
        }

        fun toMinutes(startTime: String, endTime: String): String {
            val durationString = subTimes(startTime, endTime)
            val parts = durationString.split(":")
            val hours = parts[0].toLong()
            val minutes = parts[1].toLong()
            val seconds = parts[2].toLong()
            val duration = Duration.ofHours(hours).plusMinutes(minutes).plusSeconds(seconds)
            return duration.toMinutes().toString()
        }

        fun HHmmsstoSeconds(time: String): Int {
            // "00:24:22" -> 1462
            val timeParts = time.split(":").map { it.toInt() }
            return timeParts[0] * 3600 + timeParts[1] * 60 + timeParts[2]
        }

        fun calculateAverageDuration(durations: List<String>): String {
            val totalSeconds = durations.map { HHmmsstoSeconds(it) }.sum()
            val averageSeconds = totalSeconds / durations.size
            val hours = averageSeconds / 3600
            val minutes = (averageSeconds % 3600) / 60
            val seconds = averageSeconds % 60
            return String.format("%02d Hours %02d Minutes", hours, minutes)
        }
    }
}