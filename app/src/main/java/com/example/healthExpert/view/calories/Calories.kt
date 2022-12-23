package com.example.healthExpert.view.calories

import android.content.Context
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.healthExpert.R
import com.example.healthExpert.databinding.ActivityCaloriesBinding
import com.example.healthExpert.databinding.ActivityLoginBinding
import com.example.healthExpert.view.home.Home
import com.example.healthExpert.view.login.Login
import com.example.healthExpert.widget.Ring

class Calories : AppCompatActivity() {
    private lateinit var binding: ActivityCaloriesBinding

    companion object {
        fun startFn(context: Context) {
            val intent =
                Intent(context, Calories::class.java)
            context.startActivity(intent)
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCaloriesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Set ring
        ringSetUp(binding.calories)

        binding.addBtn.setOnClickListener (View.OnClickListener { view ->
            CaloriesAdd.startFn(this)
        })

        binding.backBtn.setOnClickListener (View.OnClickListener { view ->
            finish()
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
        })
    }

    private fun ringSetUp(view: View){
        val ring = view.findViewById<Ring>(R.id.calories)
        var calories = 65
        ring.setSweepValue(calories.toFloat())
        ring.setValueText("1139")
        ring.setUnit("KCAL LEFT")
        ring.setBgColor(Color.argb(20,0, 0, 0))


















        ring.setSweepColor(Color.rgb(0, 0, 0))
    }
}