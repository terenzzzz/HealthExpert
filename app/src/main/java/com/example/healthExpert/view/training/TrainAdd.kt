package com.example.healthExpert.view.training

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.core.app.ActivityCompat
import com.example.healthExpert.R
import com.example.healthExpert.databinding.ActivityTrainAddBinding

class TrainAdd : AppCompatActivity() {
    private lateinit var binding: ActivityTrainAddBinding
    private val PERMISSION_LOCATION_GPS:Int = 1

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

        // Permission request
        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                this, arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION), PERMISSION_LOCATION_GPS
            )
        }


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