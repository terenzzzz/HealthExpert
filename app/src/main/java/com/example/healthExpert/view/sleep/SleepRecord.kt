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
    private var startTime = DateTimeConvert().toDateTime(Date())


    // Broadcast
    private lateinit var timeReceiver: BroadcastReceiver
    private lateinit var sensorReceiver: BroadcastReceiver

    //data
    private var pressureSet = mutableSetOf<Float>()
    private var temperatureSet = mutableSetOf<Float>()
    private var lightSet = mutableSetOf<Float>()
    private var humiditySet = mutableSetOf<Float>()


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

        binding.stopBtn.setOnClickListener (View.OnClickListener {
//            finish()
//            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
        })


        binding.clockRing.setBgColor(Color.rgb(174,214,207))
        sleepViewModel.timer.observe(this, Observer { item ->
            // Update the UI based on the value of MutableLiveData
            if (item != null) {
                // Update the UI
//                Log.d("SleepRecord", item)
                binding.clockRing.setValueText(item)
            }
        })

        callService()
        timeReceiver = timeBroadcastReceiverStart()
        sensorReceiver = sensorBroadcastReceiverStart()


    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(timeReceiver)
        unregisterReceiver(sensorReceiver)
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

    private fun timeBroadcastReceiverStart(): BroadcastReceiver {
        // Receive Location value from Service and update UI
        val receiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context, intent: Intent) {
                // Get location from service
                val currentTime = intent.getStringExtra("currentTime")

                sleepViewModel.updateTimer(currentTime!!)
            }

        }
        val filter = IntentFilter("timer_update")
        registerReceiver(receiver, filter)
        return receiver
    }

    private fun sensorBroadcastReceiverStart(): BroadcastReceiver {
        // Receive Location value from Service and update UI
        val receiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context, intent: Intent) {
                // Get Sensor data from service
                val temperatureSet = intent.getSerializableExtra("temperatureSet") as? Set<Float>
                if (temperatureSet != null) {
                    this@SleepRecord.temperatureSet = temperatureSet.toMutableSet()
                }


                val pressureSet = intent.getSerializableExtra("pressureSet") as? Set<Float>
                if (pressureSet != null) {
                    this@SleepRecord.pressureSet = pressureSet.toMutableSet()
                }

                val lightSet = intent.getSerializableExtra("lightSet") as? Set<Float>
                if (lightSet != null) {
                    this@SleepRecord.lightSet = lightSet.toMutableSet()
                }

                val humiditySet = intent.getSerializableExtra("humiditySet") as? Set<Float>
                if (humiditySet != null) {
                    this@SleepRecord.humiditySet = humiditySet.toMutableSet()
                }


                Log.d("测试", "temperatureSet: $temperatureSet")
                Log.d("测试", "pressureSet: $pressureSet")
                Log.d("测试", "lightSet: $lightSet")
                Log.d("测试", "humiditySet: $humiditySet")
            }
        }
        val filter = IntentFilter("sensor_update")
        registerReceiver(receiver, filter)
        return receiver
    }


}