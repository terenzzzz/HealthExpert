package com.example.healthExpert.service

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Intent
import android.content.SharedPreferences
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventCallback
import android.hardware.SensorManager
import android.os.Build
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.lifecycle.LifecycleService
import com.example.healthExpert.repository.WalkRepository
import com.example.healthExpert.view.home.Home
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.Executors
import java.util.concurrent.ScheduledExecutorService
import java.util.concurrent.TimeUnit

class StepService: LifecycleService() {
    // TODO Use PowerManager.WakeLock wakeLock to make it still running when screen off
    // TODO Bug: when screen off, it wont reset the statingsSteps
    private val CHANNEL_ID = "step notification channel id"
    private var sensorManager: SensorManager? = null
    private var stepSensor:Sensor? = null
    private var startingSteps = 0
    private var stepCount = 0
    private lateinit var stepCallback: SensorEventCallback
    private var walkRepository = WalkRepository()
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var token:String
    private var height:Float = 0f
    private var weight:Float = 0f


    override fun onCreate() {
        super.onCreate()
        Log.d("StepService", "onCreate: ")
        sharedPreferences = applicationContext.getSharedPreferences("healthy_expert", AppCompatActivity.MODE_PRIVATE)
        token = sharedPreferences.getString("token","").toString()
        height = sharedPreferences.getFloat("height",0f)
        weight = sharedPreferences.getFloat("weight",0f)


        Log.d("StepService", "onCreate()")
        startStepDetector()
        createChannel()
        val pendingIntent = createPendingIntent()
        val notification = pendingIntent?.let { createNotification(it) }
        startForeground(2, notification)

        // 定时更新
        val executor: ScheduledExecutorService = Executors.newSingleThreadScheduledExecutor()
        val runnable = Runnable {
            if(stepCount!=0){
                walkRepository.addWalkSteps(token,stepCount.toString())
                walkRepository.updateWalksOverall(token,height,weight)
                Log.d("StepService", "更新步数：$stepCount")
                startingSteps = 0
                stepCount = 0
            }
        }
        val initialDelay: Long = 0
        val period: Long = 30 // period in seconds
        executor.scheduleAtFixedRate(runnable, initialDelay, period, TimeUnit.SECONDS)
    }



    override fun onDestroy() {
        Log.d("StepService", "onDestroy: ")
        super.onDestroy()
        stopSelf()
    }


    /**
     * 获取传感器实例
     */
    private fun startStepDetector() {
        // 获取传感器管理器的实例
        sensorManager = getSystemService(SENSOR_SERVICE) as SensorManager
        stepCallback = object : SensorEventCallback(){
            override fun onSensorChanged(event: SensorEvent) {
                if (event.sensor.type === Sensor.TYPE_STEP_COUNTER) {
                    val steps = event.values[0].toInt()
                    // do something with the step count
                    if (startingSteps == 0){
                        startingSteps = steps
                    }else{
                        stepCount = steps - startingSteps
                        Log.d("Walk", "当前步数: $stepCount")
                    }
                }
            }
        }
        stepSensor = sensorManager!!.getDefaultSensor(Sensor.TYPE_STEP_COUNTER)
        sensorManager!!.registerListener(stepCallback,stepSensor,SensorManager.SENSOR_DELAY_NORMAL)
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
            Intent(this, Home::class.java),
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
            .setContentTitle("We are keep tracking your step...")
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
            val name = "Notification Channel Name"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val mChannel = NotificationChannel(CHANNEL_ID, name, importance)
            val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(mChannel)
        }
    }

}