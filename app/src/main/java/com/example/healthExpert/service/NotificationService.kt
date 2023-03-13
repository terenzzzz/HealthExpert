package com.example.healthExpert.service

import android.app.*
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat.getSystemService
import androidx.lifecycle.LifecycleService
import com.example.healthExpert.R
import com.example.healthExpert.utils.DateTimeConvert
import com.example.healthExpert.view.home.Home
import com.example.healthExpert.view.training.TrainRecord
import java.util.*


class NotificationService: LifecycleService() {
    private val CHANNEL_ID = "notification channel id"
    // Broadcast
    private lateinit var receiver: BroadcastReceiver

    private var timeList:MutableList<String> = mutableListOf()

    override fun onCreate() {
        Log.d("通知服务", "onCreate: ")
        super.onCreate()
        createChannel()
        val pendingIntentService = createPendingIntent()
        val notification = pendingIntentService?.let { createNotification(it) }
        startForeground(3, notification)

        val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val notificationIntent = Intent(this, NotificationReceiver::class.java)

        // Receive Location value from Service and update UI
        receiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context, intent: Intent) {
                // Get time from service
                val time = intent.getStringExtra("time")!!
                timeList.add(time)
                Log.d("通知服务", "timeList: $timeList")

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
    
    /**
     * A function to Create Pending Intent for creating notification for Foreground Service
     *
     * @return a pendingIntent object
     */
    private fun createPendingIntent(): PendingIntent? {
        Log.d("Service", "createPendingIntent: ")
        val pendingIntent = PendingIntent.getActivity(
            this,
            0,
            Intent(this, TrainRecord::class.java),
            PendingIntent.FLAG_IMMUTABLE
        )
        return pendingIntent
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
            .setContentTitle("We are keep tracking your location...")
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