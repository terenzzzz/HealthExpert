package com.example.healthExpert.view.medication

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.healthExpert.R
import com.example.healthExpert.databinding.ActivityCaloriesBinding
import com.example.healthExpert.databinding.ActivityMedicationBinding
import com.example.healthExpert.view.calories.Calories
import com.example.healthExpert.view.calories.CaloriesAdd
import com.example.healthExpert.view.calories.CaloriesSetting

class Medication : AppCompatActivity() {
    private lateinit var binding: ActivityMedicationBinding

    companion object {
        fun startFn(context: Context) {
            val intent =
                Intent(context, Medication::class.java)
            context.startActivity(intent)
            (context as Activity).overridePendingTransition(
                R.anim.slide_in_right,
                R.anim.slide_out_left
            )
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMedicationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.settingBtn.setOnClickListener(View.OnClickListener { view ->
            MedicationSetting.startFn(this)
        })

        binding.addBtn.setOnClickListener(View.OnClickListener { view ->
            MedicationAdd.startFn(this)
        })

        binding.backBtn.setOnClickListener(View.OnClickListener { view ->
            finish()
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
        })
    }
}