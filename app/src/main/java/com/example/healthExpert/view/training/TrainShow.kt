package com.example.healthExpert.view.training

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import com.example.healthExpert.R
import com.example.healthExpert.compatActivity.TrainingsCompatActivity
import com.example.healthExpert.databinding.ActivityCaloriesBinding
import com.example.healthExpert.databinding.ActivityTrainShowBinding
import com.example.healthExpert.model.Location
import com.example.healthExpert.view.calories.Calories
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.*

class TrainShow : TrainingsCompatActivity(), OnMapReadyCallback {
    private lateinit var binding: ActivityTrainShowBinding
    private var id:Int = -1
    private lateinit var mMap: GoogleMap

    companion object {
        fun startFn(context: Context) {
            val intent =
                Intent(context, TrainShow::class.java)
            context.startActivity(intent)
            (context as Activity).overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTrainShowBinding.inflate(layoutInflater)
        binding.lifecycleOwner = this
        binding.trainViewmodel = trainingsViewModel
        setContentView(binding.root)

        val bundle = intent.extras
        if (bundle != null) {
            id = bundle.getInt("id")
            Log.d("TrainShow", "id: $id")
        }

        binding.backBtn.setOnClickListener (View.OnClickListener { view ->
            finish()
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
        })
    }

    override fun onMapReady(p0: GoogleMap) {
        // TODO Draw line into map
        mMap = p0

    }
    private fun addMarker(latitude:Double,longitude:Double){
        val point = LatLng(latitude, longitude)
        mMap.addMarker(MarkerOptions().position(point))
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(point,15f))
    }

    /**
     * function to draw line between two location
     *
     * @param startLocation
     * @param endLocation
     */
    private fun drawLine(startLocation: Location, endLocation: Location){
        mMap.addPolyline(
            PolylineOptions()
                .add(LatLng(startLocation.latitude, startLocation.longitude), LatLng(endLocation.latitude, endLocation.longitude))
                .addSpan(StyleSpan(Color.BLUE))
        )
    }
}