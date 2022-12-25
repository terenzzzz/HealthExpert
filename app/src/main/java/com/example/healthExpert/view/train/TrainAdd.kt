package com.example.healthExpert.view.train

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.healthExpert.R
import com.example.healthExpert.databinding.ActivityTrainAddBinding
import com.example.healthExpert.databinding.ActivityTrainBinding
import com.example.healthExpert.view.calories.CaloriesAdd
import com.example.healthExpert.view.calories.CaloriesSetting

class TrainAdd : AppCompatActivity() {
    private lateinit var binding: ActivityTrainAddBinding

    companion object {
        fun startFn(context: Context) {
            val intent =
                Intent(context, TrainAdd::class.java)
            context.startActivity(intent)
            (context as Activity).overridePendingTransition(
                R.anim.slide_in_right,
                R.anim.slide_out_left
            )
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTrainAddBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.startBtn.setOnClickListener(View.OnClickListener { view ->
            TrainRecord.startFn(this)
            finish()
        })

        binding.backBtn.setOnClickListener(View.OnClickListener { view ->
            finish()
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
        })
    }
}