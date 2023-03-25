package com.example.healthExpert.view.training

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import com.example.healthExpert.R
import com.example.healthExpert.compatActivity.TrainingsCompatActivity
import com.example.healthExpert.databinding.ActivityTrainShowBinding
import com.example.healthExpert.model.Location
import com.example.healthExpert.utils.DateTimeConvert
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.google.android.material.snackbar.Snackbar
import java.text.SimpleDateFormat

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

        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        val bundle = intent.extras
        if (bundle != null) {
            id = bundle.getInt("id")
            Log.d("TrainShow", "id: $id")
            trainingsViewModel.getTrainingInfo(id)
            trainingsViewModel.getTrainingLocations(id)
        }else{
            Snackbar.make(binding.root, "Cant get the Id!", Snackbar.LENGTH_LONG).show()
        }

        trainingsViewModel.trainingInfo.observe(this) { item ->
            // Update the UI based on the value of MutableLiveData
            if (item != null) {
                val startTime = DateTimeConvert().toDateTime(item[0].StartTime);
                val endTime = DateTimeConvert().toDateTime(item[0].EndTime);
                binding.duration.text = "${DateTimeConvert().toDecimalHours(startTime,endTime)} h"
                binding.title.text = item[0].Title
                binding.speed.text = "${item[0].Speed} km/h"
                binding.distance.text = "${item[0].Distance} km"
                binding.calories.text = "${item[0].CaloriesBurn} kcal"
                binding.period.text = "$startTime - $endTime"
                when(item[0].Type){
                    "Walking" -> binding.type.setImageResource(R.drawable.walk)
                    "Running" -> binding.type.setImageResource(R.drawable.runner)
                    "Cycling" -> binding.type.setImageResource(R.drawable.cycling)
                }
            }

        }


        trainingsViewModel.trainingLocations.observe(this) { list ->
            // Update the UI based on the value of MutableLiveData
            if (list != null) {
                var lastLocation: Location? = null
                for((index, location) in list.withIndex()){
                    if (index == 0){
                        addMarker(location.latitude,location.longitude,BitmapDescriptorFactory.HUE_RED,"StartPoint")
                    }else if (index == list.lastIndex){
                        addMarker(location.latitude,location.longitude,BitmapDescriptorFactory.HUE_GREEN,"EndPoint")
                    } else{
                        if (lastLocation != null) {
                            drawLine(lastLocation,location)
                        }
                    }
                    lastLocation = location
                }
            }

        }
//        binding.deleteBtn.setOnClickListener (View.OnClickListener { view ->
//            trainingsViewModel.deleteTraining(id)
//            trainingsViewModel.updateTrainingOverall()
//            finish()
//            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
//        })

        binding.backBtn.setOnClickListener (View.OnClickListener { view ->
            finish()
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
        })
    }



    private fun addMarker(latitude:Double,longitude:Double,color: Float,title:String){
        val point = LatLng(latitude, longitude)
        val markerOptions = MarkerOptions()
            .position(point)
            .title(title)
            .icon(BitmapDescriptorFactory.defaultMarker(color))
        mMap.addMarker(markerOptions)
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
    }
}