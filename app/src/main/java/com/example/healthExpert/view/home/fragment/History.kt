package com.example.healthExpert.view.home.fragment

import android.app.DatePickerDialog
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import androidx.lifecycle.Observer
import com.example.healthExpert.compatActivity.HistoryCompatFragment
import com.example.healthExpert.databinding.FragmentHistoryBinding
import com.example.healthExpert.utils.DateTimeConvert
import com.example.healthExpert.utils.SnackbarUtil
import com.example.healthExpert.view.calories.Calories
import java.util.*
import kotlin.math.roundToInt


class History : HistoryCompatFragment(), DatePickerDialog.OnDateSetListener{
    private lateinit var binding: FragmentHistoryBinding
    private var selectedDate = DateTimeConvert().toDate(Date())

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d("History", "onCreate: ")
        super.onCreate(savedInstanceState)

        historyViewModel.caloriesAll.observe(this, Observer { item ->
            // Update the UI based on the value of MutableLiveData
            if (item != null) {
                // Update the UI
                var intake = item.Intake
                var burn = item.Burn
                var total = burn?.let { intake?.minus(it) }
                var rate = total?.div(10f)
                if (rate != null) {
                    binding.caloriesRing.setValueText("${rate.roundToInt()}%")
                    binding.caloriesRing.setSweepValue(rate.toFloat())
                }
                binding.caloriesValue.text = "${total.toString()} / 1000 kcal"
            }else{
                SnackbarUtil.buildNetwork(binding.root)
            }
        })

        historyViewModel.walkAll.observe(this, Observer { item ->
            // Update the UI based on the value of MutableLiveData
            if (item != null) {
                // Update the UI
                binding.stepsRing.setValueText("${item.TotalSteps/80}%")
                binding.stepsRing.setSweepValue((item.TotalSteps/80).toFloat())
                binding.stepsRing.setBgColor(Color.rgb(177, 169, 160))
                binding.stepsValue.text = "${ item.TotalSteps} Steps / 8000 Steps"
            }else{
                SnackbarUtil.buildNetwork(binding.root)
            }
        })

        historyViewModel.watersAll.observe(this, Observer { item ->
            // Update the UI based on the value of MutableLiveData
            if (item != null) {
                // Update the UI
                binding.drinkingRing.setSweepValue((item.Total/80).toFloat())
                binding.drinkingRing.setValueText("${item.Total/80}%")
                binding.drinkingRing.setBgColor(Color.rgb(217, 217, 217))
                binding.drinkingValue.text = "${ item.Total.toFloat() / 1000 } / 8 liters"
            }else{
                SnackbarUtil.buildNetwork(binding.root)
            }
        })

        historyViewModel.trainingAll.observe(this, Observer { item ->
            // Update the UI based on the value of MutableLiveData
            if (item != null) {
                // Update the UI
                binding.trainingRing.setSweepValue((item.Duration/1.2).toFloat())
                binding.trainingRing.setValueText("${(item.Duration/1.2).roundToInt()}%")
                binding.trainingRing.setBgColor(Color.rgb(217, 217, 217))
                binding.trainingValue.text = "${ item.Duration } minutes / 120 minutes"
            }else{
                SnackbarUtil.buildNetwork(binding.root)
            }
        })

    }


    override fun onResume() {
        Log.d("History", "onResume: ")
        super.onResume()
        historyViewModel.getCaloriesOverall(selectedDate)
        historyViewModel.getWalksOverall(selectedDate)
        historyViewModel.getWatersOverall(selectedDate)
        historyViewModel.getTrainingOverall(selectedDate)
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
            val intent = Intent(activity, Calories::class.java)
            val bundle = Bundle()
            bundle.putString("selectedDate", selectedDate)
            intent.putExtras(bundle)
            startActivity(intent)
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
        Log.d("Debug,,,,,,,,,,,,,,,,,", "onCreateView: ")
        var newMonth = month + 1
        var newMonthStr = newMonth.toString()
        if(newMonth<10){
            newMonthStr = "0$newMonth"
        }
        selectedDate = "$year-$newMonthStr-$day"
        binding.etDate.text = selectedDate


        historyViewModel.getCaloriesOverall(selectedDate)
        historyViewModel.getWalksOverall(selectedDate)
        historyViewModel.getWatersOverall(selectedDate)
        historyViewModel.getTrainingOverall(selectedDate)
    }
}

