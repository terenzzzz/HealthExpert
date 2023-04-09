package com.example.healthExpert.view.goals

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.Observer
import com.example.healthExpert.R
import com.example.healthExpert.compatActivity.GoalsCompatActivity
import com.example.healthExpert.databinding.ActivityCaloriesSettingBinding
import com.example.healthExpert.databinding.ActivityGoalsSettingBinding
import com.example.healthExpert.utils.SnackbarUtil
import com.example.healthExpert.view.calories.CaloriesSetting
import java.text.SimpleDateFormat

class GoalsSetting : GoalsCompatActivity() {
    private lateinit var binding: ActivityGoalsSettingBinding

    companion object {
        fun startFn(context: Context) {
            val intent =
                Intent(context, GoalsSetting::class.java)
            context.startActivity(intent)
            (context as Activity).overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGoalsSettingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        goalsViewModel.requestStatus.observe(this, Observer { code ->
            // Update the UI based on the value of MutableLiveData
            if (code != null){
                SnackbarUtil.buildTesting(binding.root,code)
            }
        })


        goalsViewModel.goals.observe(this, Observer { item ->
            // Update the UI based on the value of MutableLiveData
            if (item != null) {
                // Update Ring the UI
                binding.etCalories.hint = item.Calories.toString()
                binding.etSteps.hint = item.Steps.toString()
                binding.etTraining.hint = item.Training.toString()
                binding.etWater.hint = item.Water.toString()
                binding.etSleep.hint = item.Sleep.toString()
            }
        })

        binding.saveBtn.setOnClickListener {
            val etCalories = binding.etCalories.text.toString()
            val etSteps = binding.etSteps.text.toString()
            val etTraining = binding.etTraining.text.toString()
            val etWater = binding.etWater.text.toString()
            val etSleep = binding.etSleep.text.toString()

            if (etCalories!="" && etCalories != goalsViewModel.goals.value?.Calories.toString()){
                goalsViewModel.editCalories(etCalories)
            }
            if (etSteps!="" && etSteps != goalsViewModel.goals.value?.Steps.toString()){
                goalsViewModel.editSteps(etSteps)
            }
            if (etTraining!="" && etTraining != goalsViewModel.goals.value?.Training.toString()){
                goalsViewModel.editTraining(etTraining)
            }
            if (etWater!="" && etWater != goalsViewModel.goals.value?.Water.toString()){
                goalsViewModel.editWater(etWater)
            }
            if (etSleep!="" && etSleep != goalsViewModel.goals.value?.Sleep.toString()){
                goalsViewModel.editSleep(etSleep)
            }
            finish()
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
        }

        binding.backBtn.setOnClickListener {
            finish()
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
        }
    }

    override fun onResume() {
        super.onResume()
        goalsViewModel.getGoals()
    }
}