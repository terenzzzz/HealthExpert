package com.example.healthExpert.view.water

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.healthExpert.R
import com.example.healthExpert.compatActivity.WatersCompatActivity
import com.example.healthExpert.databinding.ActivityWaterBinding
import com.example.healthExpert.view.sidebar.Sidebar

class Water : WatersCompatActivity() {
    private lateinit var binding: ActivityWaterBinding

    companion object {
        fun startFn(context: Context) {
            val intent =
                Intent(context, Water::class.java)
            context.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWaterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        watersViewModel.getWaters()

        binding.backBtn.setOnClickListener (View.OnClickListener { view ->
            finish()
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
        })

        binding.settingBtn.setOnClickListener (View.OnClickListener { view ->
            WaterSetting.startFn(this)
        })

        binding.addBtn.setOnClickListener (View.OnClickListener { view ->
            WaterAdd.startFn(this)
        })
    }
}