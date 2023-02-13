package com.example.healthExpert.view.training

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.Observer
import com.example.healthExpert.R
import com.example.healthExpert.compatActivity.TrainingsCompatActivity
import com.example.healthExpert.databinding.ActivityTrainRecordBinding
import com.example.healthExpert.view.calories.CaloriesAdapter
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.squareup.picasso.Picasso

class TrainRecord : TrainingsCompatActivity(), OnMapReadyCallback {
    private lateinit var binding: ActivityTrainRecordBinding
    private lateinit var mMap: GoogleMap

    companion object {
        fun startFn(context: Context) {
            val intent =
                Intent(context, TrainRecord::class.java)
            context.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTrainRecordBinding.inflate(layoutInflater)
        binding.lifecycleOwner = this
        binding.trainingViewmodel = trainingsViewModel
        setContentView(binding.root)

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)



        binding.backBtn.setOnClickListener (View.OnClickListener { view ->
            finish()
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
        })

        binding.stopBtn.setOnClickListener (View.OnClickListener { view ->
            finish()
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
        })
    }


    override fun onResume() {
        super.onResume()

        trainingsViewModel.weather.observe(this) { item ->
            // Update the UI based on the value of MutableLiveData
            if (item != null) {
                // Update the UI
                val imageUrl = item.icon
                Picasso.get().load(imageUrl).into(binding.weatherIcon)
            }

        }
        trainingsViewModel.getWeather(53.3848,-1.4740)

    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        // Add a marker in Sydney and move the camera
        val sydney = LatLng(53.3817, -1.4819)
        mMap.addMarker(
            MarkerOptions()
                .position(sydney)
                .title("Marker in Diamond"))
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney,15f))
    }
}