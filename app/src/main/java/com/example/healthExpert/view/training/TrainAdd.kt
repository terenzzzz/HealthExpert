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
import com.example.healthExpert.view.calories.CaloriesEdit

class TrainAdd : AppCompatActivity() {
    private lateinit var binding: ActivityTrainAddBinding
    private val PERMISSION_LOCATION_GPS:Int = 1
    private lateinit var selectedType:String

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
        // Todo: Need to change default border color
        binding.cardA.background = getDrawable(R.drawable.radius_btn_gray)
        binding.cardB.background = getDrawable(R.drawable.radius_btn_gray)
        binding.cardC.background = getDrawable(R.drawable.radius_btn_gray)

        binding.cardA.setOnClickListener(View.OnClickListener { view ->
            setSelectedCard("Walking",view,binding.cardA,binding.cardB,binding.cardC)
        })
        binding.cardB.setOnClickListener(View.OnClickListener { view ->
            setSelectedCard("Running",view,binding.cardA,binding.cardB,binding.cardC)
        })
        binding.cardC.setOnClickListener(View.OnClickListener { view ->
            setSelectedCard("Cycling",view,binding.cardA,binding.cardB,binding.cardC)
        })


        binding.startBtn.setOnClickListener(View.OnClickListener { view ->
            val intent = Intent(this, TrainRecord::class.java)
            val bundle = Bundle()
            bundle.putString("type", selectedType)
            bundle.putString("title", binding.etTitle.text.toString())
            intent.putExtras(bundle)
            this.startActivity(intent)
            finish()
        })

        binding.backBtn.setOnClickListener(View.OnClickListener { view ->
            finish()
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
        })
    }

    private fun  setSelectedCard(type: String, view: View, cardA: View, cardB: View, cardC: View) {
        selectedType = type
        cardA.background = cardA.context.getDrawable(R.drawable.radius_btn_gray)
        cardB.background = cardB.context.getDrawable(R.drawable.radius_btn_gray)
        cardC.background = cardC.context.getDrawable(R.drawable.radius_btn_gray)
        view.background = view.context.getDrawable(R.drawable.radius_btn_green)
    }

}