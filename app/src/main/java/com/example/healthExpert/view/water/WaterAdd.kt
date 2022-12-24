package com.example.healthExpert.view.water

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.healthExpert.R
import com.example.healthExpert.databinding.ActivityCaloriesAddBinding
import com.example.healthExpert.databinding.ActivityWaterAddBinding

class WaterAdd : AppCompatActivity() {
    private lateinit var binding: ActivityWaterAddBinding

    companion object {
        fun startFn(context: Context) {
            val intent =
                Intent(context, WaterAdd::class.java)
            context.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWaterAddBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.backBtn.setOnClickListener (View.OnClickListener { view ->
            finish()
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
        })

        binding.addBtn.setOnClickListener (View.OnClickListener { view ->
            finish()
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
        })
    }
}