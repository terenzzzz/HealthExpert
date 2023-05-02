package com.example.healthExpert.view.home.fragment

import android.app.Activity
import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Observer
import com.example.healthExpert.compatActivity.HistoryCompatFragment
import com.example.healthExpert.databinding.FragmentHistoryBinding
import com.example.healthExpert.model.HeartRate
import com.example.healthExpert.utils.DateTimeConvert
import com.example.healthExpert.utils.SnackbarUtil
import com.example.healthExpert.view.calories.Calories
import com.example.healthExpert.view.heart.Heart
import com.example.healthExpert.view.sleep.Sleep
import com.example.healthExpert.view.training.Train
import com.example.healthExpert.view.walk.Walk
import com.example.healthExpert.view.water.Water
import java.util.*
import kotlin.math.log
import kotlin.math.roundToInt


class History : HistoryCompatFragment(), DatePickerDialog.OnDateSetListener{
    private lateinit var binding: FragmentHistoryBinding
    private lateinit var sharedPreferences: SharedPreferences
    private var selectedDate = DateTimeConvert.toDate(Date())

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d("History", "onCreate: ")
        super.onCreate(savedInstanceState)
        sharedPreferences= requireActivity().getSharedPreferences("healthy_expert", AppCompatActivity.MODE_PRIVATE)

        historyViewModel.requestStatus.observe(this, Observer { code ->
            // Update the UI based on the value of MutableLiveData
            if (code != null){
                SnackbarUtil.buildTesting(binding.root,code)
            }
        })

        historyViewModel.caloriesAll.observe(this, Observer { item ->
            // Update the UI based on the value of MutableLiveData
            if (item != null) {
                // Update the UI
                val caloriesGoal = sharedPreferences.getInt("caloriesGoal",1800)
                var intake = item.Intake
                var burn = item.Burn
                var total = burn?.let { intake?.minus(it) }
                var rate = total?.div(caloriesGoal.toFloat())?.times(100)
                if (rate != null) {
                    binding.caloriesRing.setValueText("${rate.roundToInt()}%")
                    binding.caloriesRing.setSweepValue(rate.toFloat())
                }
                binding.caloriesValue.text = "${total.toString()} kcal / ${sharedPreferences.getInt("caloriesGoal",1800)} kcal"
            }else{
                binding.caloriesRing.setValueText("0%")
                binding.caloriesRing.setSweepValue(0f)
                binding.caloriesValue.text = "0 kcl / ${sharedPreferences.getInt("caloriesGoal",1800)} kcal"
            }
        })

        historyViewModel.walkAll.observe(this, Observer { item ->
            // Update the UI based on the value of MutableLiveData
            if (item != null) {
                val stepsGoal = sharedPreferences.getInt("stepsGoal",10000)
                val rate = item.TotalSteps.toFloat() / stepsGoal.toFloat() * 100
                binding.stepsRing.setValueText(String.format("%.0f", rate)+" %")
                binding.stepsRing.setSweepValue(rate)
                binding.stepsRing.setBgColor(Color.rgb(177, 169, 160))
                binding.stepsValue.text = "${ item.TotalSteps} Steps / ${sharedPreferences.getInt("stepsGoal",10000)} Steps"
            }else{
                binding.stepsRing.setValueText("0%")
                binding.stepsRing.setSweepValue(0f)
                binding.stepsValue.text = "0 Steps / ${sharedPreferences.getInt("stepsGoal",10000)} Steps"
            }
        })

        historyViewModel.watersAll.observe(this, Observer { item ->
            // Update the UI based on the value of MutableLiveData
            if (item != null) {
                // Update the UI
                val waterGoal = sharedPreferences.getInt("waterGoal",2000)
                val rate = item.Total.toFloat()/waterGoal.toFloat()*100f
                binding.drinkingRing.setSweepValue(rate)
                binding.drinkingRing.setValueText(String.format("%.0f", rate)+" %")
                binding.drinkingRing.setBgColor(Color.rgb(217, 217, 217))
                binding.drinkingValue.text = "${ item.Total} ml / ${sharedPreferences.getInt("waterGoal",2000)} ml"
            } else{
                binding.drinkingRing.setValueText("0%")
                binding.drinkingRing.setSweepValue(0f)
                binding.drinkingValue.text = "0 ml / ${sharedPreferences.getInt("waterGoal",2000)} ml"
            }
        })

        historyViewModel.trainingAll.observe(this, Observer { item ->
            // Update the UI based on the value of MutableLiveData
            if (item != null) {
                // Update the UI
                val trainingGoal = sharedPreferences.getInt("trainingGoal",60)
                var rate = item.Duration.toFloat() / trainingGoal.toFloat() * 100f
                binding.trainingRing.setSweepValue(rate)
                binding.trainingRing.setValueText(String.format("%.0f", rate)+" %")
                binding.trainingRing.setBgColor(Color.rgb(217, 217, 217))
                binding.trainingValue.text = "${ item.Duration } minutes / ${sharedPreferences.getInt("trainingGoal",60)} minutes"
            }else{
                binding.trainingRing.setValueText("0%")
                binding.trainingRing.setSweepValue(0f)
                binding.trainingValue.text = "0 minutes / ${sharedPreferences.getInt("trainingGoal",60)} minutes"
            }
        })

        historyViewModel.sleep.observe(this, Observer { item ->
            // Update the UI based on the value of MutableLiveData
            if (item != null) {
                // Update the UI
                val sleepGoal = sharedPreferences.getInt("sleepGoal",8)
                val startTime = DateTimeConvert.toDateTime(item.StartTime)
                val endTime = DateTimeConvert.toDateTime(item.EndTime)
                val duration = DateTimeConvert.toDecimalHours(startTime,endTime)
                var rate = duration.toFloat() / sleepGoal.toFloat() * 100f
                binding.sleepRing.setSweepValue(rate)
                binding.sleepRing.setValueText(String.format("%.0f", rate)+" %")
                binding.sleepRing.setBgColor(Color.rgb(217, 217, 217))
                binding.sleepValue.text = "$duration Hours / ${sharedPreferences.getInt("sleepGoal",8)} Hours"
            }else{
                binding.sleepRing.setValueText("0%")
                binding.sleepRing.setSweepValue(0f)
                binding.sleepValue.text = "0.00 Hours / ${sharedPreferences.getInt("sleepGoal",8)} Hours"
            }
        })

        historyViewModel.heartRate.observe(this, Observer { heartRates ->
            // Update the UI based on the value of MutableLiveData
            Log.d("测试", "heartRates: $heartRates")
            if (heartRates != null && heartRates.isNotEmpty()) {
                val avg = "Avg: ${String.format("%.0f", heartRates.map { it.HeartRate.toInt() }.average())} BPM"
                val max = "Max: ${heartRates.maxBy { it.HeartRate.toInt() }.HeartRate} BPM"
                binding.bpmValue.text = "$avg / $max"
            }else{
                binding.bpmValue.text = "Avg: 0 BPM / Max: 0 BPM"
            }
        })

    }


    override fun onResume() {
        Log.d("History", "onResume: ")
        super.onResume()
        binding.sleepRing.setSweepValue(0f)
        binding.sleepRing.setValueText("0 %")
        binding.sleepRing.setBgColor(Color.rgb(217, 217, 217))
        historyViewModel.getCaloriesOverall(selectedDate)
        historyViewModel.getWalksOverall(selectedDate)
        historyViewModel.getWatersOverall(selectedDate)
        historyViewModel.getTrainingOverall(selectedDate)
        historyViewModel.getSleep(selectedDate)
        historyViewModel.getHeartRate(selectedDate)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.d("History", "onCreateView: ")
        // Inflate the layout for this fragment
        binding = FragmentHistoryBinding.inflate(layoutInflater)

        binding.etDate.text = selectedDate

        binding.etDate.setOnClickListener (View.OnClickListener {
            showDatePicker()
        })

        binding.caloriesCard.setOnClickListener {
            activity?.let { it1 -> goToActivity(it1,Calories::class.java) }
        }

        binding.stepsCard.setOnClickListener {
            activity?.let { it1 -> goToActivity(it1,Walk::class.java) }
        }

        binding.waterCard.setOnClickListener {
            activity?.let { it1 -> goToActivity(it1,Water::class.java) }
        }

        binding.trainingCard.setOnClickListener {
            activity?.let { it1 -> goToActivity(it1,Train::class.java) }
        }

        binding.sleepCard.setOnClickListener {
            activity?.let { it1 -> goToActivity(it1,Sleep::class.java) }
        }

        binding.hrCard.setOnClickListener {
            activity?.let { it1 -> goToActivity(it1,Heart::class.java) }
        }


        return binding.root
    }

    private fun showDatePicker() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val dialog = DatePickerDialog(requireContext(), this, year, month, day)
        dialog.datePicker.maxDate = calendar.timeInMillis
        dialog.show()
    }


    override fun onDateSet(view: DatePicker, year: Int, month: Int, day: Int) {

        var newDay = day
        var newMonth = month + 1
        var newMonthStr = newMonth.toString()
        var newDayStr = newDay.toString()
        if(newMonth<10){
            newMonthStr = "0$newMonth"
        }
        if(newDay<10){
            newDayStr = "0$newDay"
        }
        selectedDate = "$year-$newMonthStr-$newDayStr"
        binding.etDate.text = selectedDate


        historyViewModel.getCaloriesOverall(selectedDate)
        historyViewModel.getWalksOverall(selectedDate)
        historyViewModel.getWatersOverall(selectedDate)
        historyViewModel.getTrainingOverall(selectedDate)
        historyViewModel.getSleep(selectedDate)
        historyViewModel.getHeartRate(selectedDate)
    }

    private fun goToActivity(activity: FragmentActivity, targetActivity: Class<out Activity>) {
        val intent = Intent(activity, targetActivity)
        val bundle = Bundle()
        bundle.putString("selectedDate", selectedDate)
        intent.putExtras(bundle)
        startActivity(intent)
    }

}

