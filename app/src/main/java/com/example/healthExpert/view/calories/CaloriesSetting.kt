package com.example.healthExpert.view.calories

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.healthExpert.R
import com.example.healthExpert.compatActivity.CaloriesCompatActivity
import com.example.healthExpert.databinding.ActivityCaloriesBinding
import com.example.healthExpert.databinding.ActivityCaloriesSettingBinding

class CaloriesSetting : CaloriesCompatActivity() {
    private lateinit var binding: ActivityCaloriesSettingBinding

    companion object {
        fun startFn(context: Context) {
            val intent =
                Intent(context, CaloriesSetting::class.java)
            context.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCaloriesSettingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.backBtn.setOnClickListener {
            finish()
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
        }

        binding.saveBtn.setOnClickListener {
            caloriesViewModel
            finish()
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
        }
    }
}