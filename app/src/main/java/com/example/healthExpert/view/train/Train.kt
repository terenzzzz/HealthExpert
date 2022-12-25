package com.example.healthExpert.view.train

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.healthExpert.R
import com.example.healthExpert.databinding.ActivityCaloriesBinding
import com.example.healthExpert.databinding.ActivityTrainBinding
import com.example.healthExpert.view.calories.CaloriesAdd
import com.example.healthExpert.view.calories.CaloriesSetting
import com.example.healthExpert.widget.Ring
import com.example.healthExpert.widget.RingWalking

class Train : AppCompatActivity() {
    private lateinit var binding: ActivityTrainBinding

    companion object {
        fun startFn(context: Context) {
            val intent =
                Intent(context, Train::class.java)
            context.startActivity(intent)
            (context as Activity).overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTrainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Set ring
        ringSetUp(binding.calories,100)

        binding.settingBtn.setOnClickListener (View.OnClickListener { view ->
            TrainSetting.startFn(this)
        })

        binding.addBtn.setOnClickListener (View.OnClickListener { view ->
            TrainAdd.startFn(this)
        })

        binding.backBtn.setOnClickListener (View.OnClickListener { view ->
            finish()
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
        })
    }

    private fun ringSetUp(view: View, value: Int){
        val ring = view.findViewById<RingWalking>(R.id.calories)
        var calories = value
        ring.setSweepValue(calories.toFloat())
        ring.setValueText("1h 42m")
        ring.setStateText("Active")
        ring.setUnit("Training Goal: ")
        ring.setUnitValue("1 hours")
        ring.setBgColor(Color.argb(20,0, 0, 0))
        ring.setSweepColor(Color.rgb(0, 0, 0))
    }
}