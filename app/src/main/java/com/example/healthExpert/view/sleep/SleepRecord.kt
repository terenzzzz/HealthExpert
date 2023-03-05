package com.example.healthExpert.view.sleep

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Color
import android.location.Location
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import androidx.lifecycle.Observer
import com.example.healthExpert.R
import com.example.healthExpert.compatActivity.SleepCompatActivity
import com.example.healthExpert.databinding.ActivitySleepRecordBinding
import com.example.healthExpert.service.LocationService
import com.example.healthExpert.service.SleepService
import com.example.healthExpert.utils.DateTimeConvert
import java.util.*

class SleepRecord : SleepCompatActivity() {
    private lateinit var binding: ActivitySleepRecordBinding
    private lateinit var mHandler: Handler
    private lateinit var mRunnable: Runnable
    private var mCounter: Int = 0

    // Broadcast
    private lateinit var receiver: BroadcastReceiver

    companion object {
        fun startFn(context: Context) {
            val intent =
                Intent(context, SleepRecord::class.java)
            context.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySleepRecordBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.backBtn.setOnClickListener (View.OnClickListener { view ->
            finish()
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
        })


        binding.clockRing.setBgColor(Color.rgb(174,214,207))
        sleepViewModel.time.observe(this, Observer { item ->
            // Update the UI based on the value of MutableLiveData
            if (item != null) {
                // Update the UI
//                Log.d("SleepRecord", item)
                binding.clockRing.setValueText(item)
            }
        })

        callService()
        broadcastReceiverStart()

//        mHandler = Handler(Looper.getMainLooper())
//        mRunnable = object : Runnable {
//            override fun run() {
//                // Execute your method here
//                sleepViewModel.updateTimer(DateTimeConvert().toTime(Date()))
//
//                // Schedule the next execution of this Runnable in 1 second
//                mHandler.postDelayed(this, 1000)
//            }
//        }
//
//        // Start the timer
//        mHandler.post(mRunnable)

    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(receiver)
        stopService()
    }

    /**
     * function to call the Location Service to start
     */
    private fun callService(){
        Log.d("测试", "callService: ")
        Intent(this, SleepService::class.java).apply {
            startService(this)
        }
    }

    /**
     * function to stop Service when is no longer needed
     */
    private fun stopService(){
        Intent(this, SleepService::class.java).apply {
            stopService(this)
        }
    }

    private fun broadcastReceiverStart(){
        // Receive Location value from Service and update UI
        receiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context, intent: Intent) {
                // Get location from service
                val currentTime = intent.getStringExtra("currentTime")
                Log.d("测试", "onReceive: $currentTime")
                sleepViewModel.updateTimer(DateTimeConvert().toTime(Date()))
            }

        }
        val filter = IntentFilter("timer_update")
        registerReceiver(receiver, filter)
    }

}