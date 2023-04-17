package com.example.healthExpert.service

import android.app.*
import android.content.*
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventCallback
import android.hardware.SensorManager
import android.os.Build
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.lifecycle.LifecycleService
import androidx.lifecycle.lifecycleScope
import com.example.healthExpert.R
import com.example.healthExpert.repository.WalkRepository
import com.example.healthExpert.view.home.Home
import com.example.healthExpert.view.medication.Medication
import com.example.healthExpert.view.water.Water
import kotlinx.coroutines.*
import java.util.*
import java.util.concurrent.Executors
import java.util.concurrent.ScheduledExecutorService
import java.util.concurrent.TimeUnit

class StepService: LifecycleService() {
    // For Steps
    private val CHANNEL_ID = "2"
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

    // For Medication Notification
    private lateinit var receiver: BroadcastReceiver
    private var timeList:MutableList<String> = mutableListOf()


    override fun onCreate() {
        super.onCreate()
        Log.d("StepService", "onCreate: ")
        sharedPreferences = applicationContext.getSharedPreferences("healthy_expert", AppCompatActivity.MODE_PRIVATE)
        token = sharedPreferences.getString("token","").toString()
        height = sharedPreferences.getFloat("height",0f)
        weight = sharedPreferences.getFloat("weight",0f)

        Log.d("StepService", "onCreate()")
        createChannel()
        startForeground(2, createNotification(createPendingIntent()))

        startStepDetector()

        // Drinking Notification push
        val timer = Timer()
        timer.schedule(object : TimerTask() {
            override fun run() {
                // 在这里执行定时器任务
                drinkingNotification()
            }
        }, 2*60*60*1000, 2*60*60*1000)

        // 定时更新
        val executor: ScheduledExecutorService = Executors.newSingleThreadScheduledExecutor()
        val runnable = Runnable {
            if(stepCount!=0){


                walkRepository.addWalkSteps(token, stepCount.toString()) { resStatus ->
                    if (resStatus == 200) {
                        // 处理请求成功的情况
                        walkRepository.updateWalksOverall(token,weight,height)
                        Log.d("StepService", "更新步数：$stepCount")
                        startingSteps = 0
                        stepCount = 0
                    } else {
                        // 处理请求失败的情况
                        Log.d("StepService", "startingSteps：$startingSteps")
                        Log.d("StepService", "stepCount：$stepCount")

                    }
                }
            }
        }
        val initialDelay: Long = 0
        val period: Long = 30 // period in seconds
        executor.scheduleAtFixedRate(runnable, initialDelay, period, TimeUnit.SECONDS)

        // Medication Notification
        val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val notificationIntent = Intent(this, NotificationReceiver::class.java)
        receiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context, intent: Intent) {
                // Get time from service
                val time = intent.getStringExtra("time")!!
                timeList.add(time)

                val timeObj = time.split(":")
                val pendingIntent = PendingIntent.getBroadcast(context, timeList.size, notificationIntent, PendingIntent.FLAG_IMMUTABLE)

                val calendar = Calendar.getInstance()
                calendar.set(Calendar.HOUR_OF_DAY, timeObj[0].toInt()) // 设置小时数为 8
                calendar.set(Calendar.MINUTE, timeObj[1].toInt()) // 设置分钟数为 30
                calendar.set(Calendar.SECOND, timeObj[2].toInt()) // 设置秒数为 0
                calendar.set(Calendar.MILLISECOND, 0) // 设置毫秒数为 0
                var triggerAtMillis = calendar.timeInMillis

                if (triggerAtMillis > System.currentTimeMillis()) {
                    // 如果指定时间还没到，则添加通知
                    alarmManager.setExact(AlarmManager.RTC_WAKEUP, triggerAtMillis, pendingIntent)
                }
            }
        }
        val filter = IntentFilter("medication")
        registerReceiver(receiver, filter)
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
    private fun createPendingIntent(): PendingIntent {
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
            .setPriority(NotificationCompat.PRIORITY_HIGH)
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
            val mChannel = NotificationChannel(CHANNEL_ID, "Steps and Notification", NotificationManager.IMPORTANCE_DEFAULT)
            val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(mChannel)
        }
    }

    fun drinkingNotification(){
        var manager = this.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        //检查版本
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            //生成Channel
            val notificationChannel =
                NotificationChannel("drinkChannel", "Water Drinking Notification", NotificationManager.IMPORTANCE_HIGH)
            //添加Channel到manager
            manager!!.createNotificationChannel(notificationChannel)
        }
        val intent = Intent(this, Water::class.java)

        //生成intent，让通知可以点击回到主页面
        val pendingIntent = PendingIntent.getActivity(this, 223, intent,
            PendingIntent.FLAG_IMMUTABLE
        )
        //检查版本
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            //生成notication模板
            val notification = Notification.Builder(this, "drinkChannel")
                .setAutoCancel(true)
                .setContentIntent(pendingIntent)
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(R.drawable.icon)
                .setContentTitle("Water Drinking Reminder")
                .setContentText("It's time to get some water!")
                .setPriority(Notification.PRIORITY_HIGH)
                .build()
            //添加notication模板到manager
            manager!!.notify(12, notification)
        }
    }
}

class NotificationReceiver : BroadcastReceiver() {
    private var manager: NotificationManager? = null

    override fun onReceive(context: Context?, intent: Intent?) {
        // 在这里推送通知
        //生成manager
        manager = context?.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        //检查版本
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            //生成Channel
            val notificationChannel =
                NotificationChannel("testChannelId", "Medical Notification", NotificationManager.IMPORTANCE_HIGH)
            //添加Channel到manager
            manager!!.createNotificationChannel(notificationChannel)
        }
        //生成intent，让通知可以点击回到主页面
        val pendingIntent = PendingIntent.getActivity(context, 222, intent,
            PendingIntent.FLAG_IMMUTABLE
        )
        //检查版本
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            //生成notication模板
            val notification = Notification.Builder(context, "testChannelId")
                .setAutoCancel(true)
                .setContentIntent(pendingIntent)
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(R.drawable.icon)
                .setContentTitle("Medication Reminder")
                .setContentText("You have some medicine you may need to take!")
                .setPriority(Notification.PRIORITY_HIGH)
                .build()
            //添加notication模板到manager
            manager!!.notify(11, notification)
        }
    }
}