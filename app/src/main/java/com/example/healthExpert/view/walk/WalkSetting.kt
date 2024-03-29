package com.example.healthExpert.view.walk

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.healthExpert.R
import com.example.healthExpert.databinding.ActivityCaloriesSettingBinding
import com.example.healthExpert.databinding.ActivityWalkSettingBinding

class WalkSetting : AppCompatActivity() {
    private lateinit var binding: ActivityWalkSettingBinding

    companion object {
        fun startFn(context: Context) {
            val intent =
                Intent(context, WalkSetting::class.java)
            context.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWalkSettingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.backBtn.setOnClickListener (View.OnClickListener { view ->
            finish()
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
        })

        binding.saveBtn.setOnClickListener (View.OnClickListener { view ->
            finish()
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
        })
    }
}