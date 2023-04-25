package com.example.healthExpert.view.sleep

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.example.healthExpert.R
import com.example.healthExpert.compatActivity.SleepCompatActivity
import com.example.healthExpert.databinding.ActivitySleepBinding
import com.example.healthExpert.utils.AlertDialog
import com.example.healthExpert.utils.DateTimeConvert
import com.example.healthExpert.utils.SnackbarUtil
import java.util.*


class Sleep : SleepCompatActivity() {
    private lateinit var binding: ActivitySleepBinding
    private lateinit var sharedPreferences: SharedPreferences
    private var todayDate = DateTimeConvert.toDate(Date())
    var mode = "edit"

    companion object {
        fun startFn(context: Context) {
            val intent =
                Intent(context, Sleep::class.java)
            context.startActivity(intent)
            (context as Activity).overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySleepBinding.inflate(layoutInflater)
        setContentView(binding.root)
        sharedPreferences= this.getSharedPreferences("healthy_expert", AppCompatActivity.MODE_PRIVATE)
        binding.date.text = todayDate

        sleepViewModel.requestStatus.observe(this, Observer { code ->
            // Update the UI based on the value of MutableLiveData
            if (code != null){
                SnackbarUtil.buildTesting(binding.root,code)
            }
        })

        sleepViewModel.avgSleep.observe(this, Observer { item ->
            // Update the UI based on the value of MutableLiveData
            if (item != null){
                binding.avgSleep.text = item
            }
        })

        val bundle = intent.extras
        if (bundle != null && bundle.getString("selectedDate") != "") {
            todayDate = bundle.getString("selectedDate").toString()
            binding.date.text = todayDate
            mode = "view"
            binding.addBtn.visibility = View.GONE
        }


        binding.addBtn.setOnClickListener (View.OnClickListener { view ->
            SleepRecord.startFn(this)
        })

        binding.backBtn.setOnClickListener (View.OnClickListener { view ->
            finish()
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
        })

        binding.humidityInfo.setOnClickListener {
            val title = "Humidity Info"
            val message = "Medical research shows that the best humidity range for the human body is 45%-60%."
            AlertDialog().showDialog(this,title,message)
        }

        binding.temperatureInfo.setOnClickListener {
            val title = "Temperature Info"
            val message = "According to relevant research: when the bedroom temperature is 20-25℃, " +
                    "the human body feels the most comfortable."
            AlertDialog().showDialog(this,title,message)
        }

        binding.pressureInfo.setOnClickListener {
            val title = "Pressure Info"
            val message = "People feel sleepy when air pressure is higher or lower than normal"
            AlertDialog().showDialog(this,title,message)
        }

        binding.lightInfo.setOnClickListener {
            val title = "Light Info"
            val message = "Light is a very strong signal. When it enters the eyes, it will interfere with the sleep mechanism in the brain, " +
                    "reduce the amount of melatonin secretion, and affect the depth and quality of sleep"
            AlertDialog().showDialog(this,title,message)
        }

        sleepViewModel.sleep.observe(this, Observer { item ->
            // Update the UI based on the value of MutableLiveData
            if (item != null){
                binding.date.text = DateTimeConvert.toDate(item.StartTime)
                binding.startTime.text = DateTimeConvert.toHHmm(item.StartTime)
                binding.endTime.text = DateTimeConvert.toHHmm(item.EndTime)
                binding.startDate.text = DateTimeConvert.toDate(item.StartTime)
                binding.endDate.text = DateTimeConvert.toDate(item.EndTime)
                binding.humidityValue.text =  String.format("%.2f", item.Humidity)
                binding.temperatureValue.text = String.format("%.2f", item.Temperature)
                binding.pressureValue.text = String.format("%.2f", item.Pressure)
                binding.lightValue.text = String.format("%.2f", item.Light)
                binding.durationValue.text = DateTimeConvert.subTimes(DateTimeConvert.toDateTime(item.StartTime),
                    DateTimeConvert.toDateTime(item.EndTime))

                // progress Bar
                val sleepGoal = sharedPreferences.getInt("sleepGoal",8)
                val duration = DateTimeConvert.toDecimalHours(DateTimeConvert.toDateTime(item.StartTime),
                    DateTimeConvert.toDateTime(item.EndTime))

                val progress = duration.toFloat() / sleepGoal.toFloat() * 100
                binding.sleepProgress.progress = progress.toInt()
                binding.sleepRate.text = String.format("%.2f", progress)+" %"
            }
        })

    }

    override fun onResume() {
        super.onResume()
        sleepViewModel.getSleep(todayDate)
        sleepViewModel.getLastFive()
    }
}