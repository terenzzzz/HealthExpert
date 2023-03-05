package com.example.healthExpert.service

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventCallback
import android.hardware.SensorManager
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.lifecycle.LifecycleService
import com.example.healthExpert.utils.DateTimeConvert
import com.example.healthExpert.view.sleep.SleepRecord
import java.util.*
import kotlin.math.abs


class SleepService:LifecycleService() {
    private val CHANNEL_ID = "sleep notification channel id"
    private var startDateTime = Date()
    private var startTime = DateTimeConvert().toDateTime(startDateTime)
    private var currentTime = ""
    private var endTime = ""

    private lateinit var timerHandler: Handler
    private lateinit var timerRunnable: Runnable

    // data
    private var lastPressure = 0F
    private var lastTemperature = 0F
    private var lastLight = 0F
    private var lastHumidity = 0F

    val pressureSet = mutableSetOf<Float>()
    val temperatureSet = mutableSetOf<Float>()
    val lightSet = mutableSetOf<Float>()
    val humiditySet = mutableSetOf<Float>()

    // Sensor
    private lateinit var sensorManager: SensorManager
    private var temperatureSensor: Sensor?=null
    private lateinit var temperatureCallback: SensorEventCallback
    private var pressureSensor: Sensor?=null
    private lateinit var pressureCallback: SensorEventCallback
    private var lightSensor: Sensor?=null
    private lateinit var lightCallback: SensorEventCallback
    private var humiditySensor: Sensor?=null
    private lateinit var humidityCallback: SensorEventCallback



    override fun onCreate() {
        super.onCreate()

        Log.d("服务", "onCreate() $startTime")
        createChannel()
        val pendingIntent = createPendingIntent()
        val notification = pendingIntent?.let { createNotification(it) }
        startForeground(3, notification)

        startTimer()
        startSensor()

    }


    override fun onDestroy() {
        Log.d("SleepService", "onDestroy: ")
        super.onDestroy()
        timerHandler.removeCallbacks(timerRunnable)
        sensorManager.unregisterListener(temperatureCallback)
        sensorManager.unregisterListener(pressureCallback)
        sensorManager.unregisterListener(lightCallback)
        sensorManager.unregisterListener(humidityCallback)

        stopSelf()

    }


    /**
     * 获取传感器实例
     */
    private fun startSensor(){
        // Init sensorManager
        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager


        temperatureCallback = getTemperatureCallback()
        pressureCallback = getPressureCallback()
        lightCallback = getLightCallback()
        humidityCallback = getHumidityCallback()

        temperatureSensor = sensorManager.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE)
        sensorManager.registerListener(temperatureCallback,temperatureSensor,SensorManager.SENSOR_DELAY_NORMAL)

        pressureSensor = sensorManager.getDefaultSensor(Sensor.TYPE_PRESSURE)
        sensorManager.registerListener(pressureCallback,pressureSensor,SensorManager.SENSOR_DELAY_NORMAL)

        lightSensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT)
        sensorManager.registerListener(lightCallback,lightSensor,SensorManager.SENSOR_DELAY_NORMAL)

        humiditySensor = sensorManager.getDefaultSensor(Sensor.TYPE_RELATIVE_HUMIDITY)
        sensorManager.registerListener(humidityCallback,humiditySensor,SensorManager.SENSOR_DELAY_NORMAL)
    }

    private fun getTemperatureCallback(): SensorEventCallback {
        // Handle Temperature update
         val temperatureCallback = object : SensorEventCallback(){
            override fun onSensorChanged(event: SensorEvent?) {

                val currentTemperature = String.format("%.2f", event?.values?.get(0)).toFloat()
                if (currentTemperature != null && abs(currentTemperature - lastTemperature) > 0.1f) {
                    lastTemperature = currentTemperature
                    temperatureSet.add(lastTemperature)
//                    Log.d("服务", "temperatureSet: $temperatureSet")

                    brocasting()
                }
            }
        }
        return temperatureCallback
    }

    private fun getPressureCallback(): SensorEventCallback {
        // Handle Pressure update
        val pressureCallback = object : SensorEventCallback(){
            override fun onSensorChanged(event: SensorEvent?) {
                val currentPressure = event?.values?.get(0)
                if (currentPressure != null && abs(currentPressure - lastPressure) > 0.1f) {
                    // process the new pressure value
                    lastPressure = currentPressure
                    pressureSet.add(lastPressure)
//                    Log.d("服务", "lastPressure: ${pressureSet}")

                    brocasting()

                }
            }
        }
        return pressureCallback
    }

    private fun getLightCallback(): SensorEventCallback {
        // Handle Pressure update
        val lightCallback = object : SensorEventCallback(){
            override fun onSensorChanged(event: SensorEvent?) {
                val currentLight = event?.values?.get(0)
                if (currentLight != null && abs(currentLight - lastLight) > 0.1f) {
                    // process the new pressure value
                    lastLight = currentLight
                    lightSet.add(lastLight)
//                    Log.d("服务", "lightSet: ${lightSet}")

                    brocasting()

                }
            }
        }
        return lightCallback
    }

    private fun getHumidityCallback(): SensorEventCallback {
        // Handle Pressure update
        val humidityCallback = object : SensorEventCallback(){
            override fun onSensorChanged(event: SensorEvent?) {
                val currentHumidity = event?.values?.get(0)
                if (currentHumidity != null && abs(currentHumidity - lastHumidity) > 0.1f) {
                    // process the new pressure value
                    lastHumidity = currentHumidity
                    humiditySet.add(lastHumidity)
//                    Log.d("服务", "lightSet: ${lightSet}")

                    brocasting()
                }
            }
        }
        return humidityCallback
    }



    private fun startTimer(){
        timerHandler = Handler(Looper.getMainLooper())
        timerRunnable = object : Runnable {
            override fun run() {
                // Execute your code here

                currentTime = DateTimeConvert().toDateTime(Date())
                val timeDifference = DateTimeConvert().subTimes(startTime,currentTime)


                val intent = Intent("timer_update")
                intent.putExtra("currentTime", timeDifference)
                sendBroadcast(intent)
                // Schedule the next execution of this Runnable in 1 second
                timerHandler.postDelayed(this, 1000)
            }
        }

        // Start the timer
        timerHandler.post(timerRunnable)
    }

    private fun brocasting(){
        val intent = Intent("sensor_update")
        intent.putExtra("temperatureSet", temperatureSet as java.io.Serializable)
        intent.putExtra("pressureSet", pressureSet as java.io.Serializable)
        intent.putExtra("lightSet", lightSet as java.io.Serializable)
        intent.putExtra("humiditySet", humiditySet as java.io.Serializable)
        sendBroadcast(intent)
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