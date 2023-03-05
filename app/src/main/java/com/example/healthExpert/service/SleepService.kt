package com.example.healthExpert.service

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.lifecycle.LifecycleService
import com.example.healthExpert.utils.DateTimeConvert
import com.example.healthExpert.view.sleep.SleepRecord
import java.util.*
import kotlin.math.log

class SleepService:LifecycleService() {
    private val CHANNEL_ID = "sleep notification channel id"
    private var startDateTime = Date()
    private var startTime = DateTimeConvert().toTime(startDateTime)
    private var currentTime = ""
    private var endTime = ""

    private lateinit var timerHandler: Handler
    private lateinit var timerRunnable: Runnable


    override fun onCreate() {
        super.onCreate()

        Log.d("服务", "onCreate() $startTime")
        startStepDetector()
        createChannel()
        val pendingIntent = createPendingIntent()
        val notification = pendingIntent?.let { createNotification(it) }
        startForeground(3, notification)

        startTimer()
    }


    override fun onDestroy() {
        Log.d("SleepService", "onDestroy: ")
        super.onDestroy()
        timerHandler.removeCallbacks(timerRunnable)
        stopSelf()

    }



    /**
     * 获取传感器实例
     */
    private fun startStepDetector() {
        // 获取传感器管理器的实例


    }

    private fun startTimer(){
        timerHandler = Handler(Looper.getMainLooper())
        timerRunnable = object : Runnable {
            override fun run() {
                // Execute your code here
                currentTime = DateTimeConvert().toTime(Date())
                Log.d("服务", "startTimer() $currentTime")
                val intent = Intent("timer_update")
                intent.putExtra("currentTime", currentTime)
                sendBroadcast(intent)

                // Schedule the next execution of this Runnable in 1 second
                timerHandler.postDelayed(this, 1000)
            }
        }

        // Start the timer
        timerHandler.post(timerRunnable)
    }

    /**
     * A function to Create Pending Intent for creating notification for Foreground Service
     *
     * @return a pendingIntent object
     */
    private fun createPendingIntent(): PendingIntent? {
        Log.d("Service", "createPendingIntent: ")
        return PendingIntent.getActivity(
            this,
            0,
            Intent(this, SleepRecord::class.java),
            PendingIntent.FLAG_IMMUTABLE
        )
    }

    /**
     * A function to Create notification for Foreground Service
     *
     * @param pendingIntent
     *
     * @return a pendingIntent object
     */
    private fun createNotification(pendingIntent: PendingIntent): Notification {
        Log.d("Service", "createNotification: ")
        val notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("We are keep tracking your sleep...")
            .setContentText("Click here to come back")
            .setContentIntent(pendingIntent)
            .build()
        return notification
    }

    /**
     * A function to Create the NotificationChannel
     */
    private fun createChannel() {
        Log.d("Service", "createChannel: ")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // Create the NotificationChannel
            val name = "Sleep Notification Channel"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val mChannel = NotificationChannel(CHANNEL_ID, name, importance)
            val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(mChannel)
        }
    }
}