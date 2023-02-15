package com.example.healthExpert.view.training

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.healthExpert.R
import com.example.healthExpert.compatActivity.TrainingsCompatActivity
import com.example.healthExpert.databinding.ActivityCaloriesBinding
import com.example.healthExpert.databinding.ActivityTrainShowBinding
import com.example.healthExpert.view.calories.Calories

class TrainShow : TrainingsCompatActivity() {
    private lateinit var binding: ActivityTrainShowBinding

    companion object {
        fun startFn(context: Context) {
            val intent =
                Intent(context, TrainShow::class.java)
            context.startActivity(intent)
            (context as Activity).overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTrainShowBinding.inflate(layoutInflater)
        binding.lifecycleOwner = this
        binding.trainViewmodel = trainingsViewModel
        setContentView(binding.root)
    }
}