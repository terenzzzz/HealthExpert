package com.example.healthExpert.view.training

import android.Manifest
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.app.ActivityCompat
import com.example.healthExpert.R
import com.example.healthExpert.compatActivity.TrainingsCompatActivity
import com.example.healthExpert.databinding.ActivityTrainRecordBinding
import com.example.healthExpert.model.Location
import com.example.healthExpert.service.LocationService
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.google.android.material.snackbar.Snackbar
import com.squareup.picasso.Picasso

class TrainRecord : TrainingsCompatActivity(), OnMapReadyCallback {
    private lateinit var binding: ActivityTrainRecordBinding
    private lateinit var mMap: GoogleMap

    //    Location
    private var lastLocation: Location? = null
    private var headMarker:Marker? = null
    private var locations: MutableList<Location> = mutableListOf()
    private var id:Int = -1
    private var type:String = ""
    private var title:String = ""

    // Broadcast
    private lateinit var receiver: BroadcastReceiver


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

        // get Data from add Activity
        val bundle = intent.extras
        if (bundle != null) {
            id = bundle.getInt("id")
            type = bundle.getString("type").toString()
            title = bundle.getString("title").toString()
            Log.d("TrainRecord", "id: $id")
            Log.d("TrainRecord", "type: $type")
            Log.d("TrainRecord", "tile: $title")
        }

        // Call Service
        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
            callService()
        }else{
            Snackbar.make(binding.root, "Please give Permission of Location", Snackbar.LENGTH_LONG).show()
        }

        // Adding map to the view
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        // Receive Location value from Service and update UI
        receiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context, intent: Intent) {
                // Get location from service
                val latitude = intent.getStringExtra("latitude")!!
                val longitude = intent.getStringExtra("longitude")!!
                Log.d("TripActivity", "latitude: $latitude, longitude: $longitude")
                // Safe Location to a List
                val location = Location(latitude.toDouble(),longitude.toDouble())
                locations.add(location)

                // Update Path and Current Location
                if (lastLocation != null){
                    drawLine(lastLocation!!,location)
                    addDot(location.latitude,location.longitude)
                }else{
                    // Init Start Point
                    addMarker(location!!.latitude, location!!.longitude)
                    addDot(location!!.latitude, location!!.longitude)
                }
                lastLocation = location
            }
        }
        val filter = IntentFilter("update-ui")
        registerReceiver(receiver, filter)



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

    override fun onDestroy() {
        Log.d("TripActivity", "onDestroy: ")
        super.onDestroy()
        stopService()
        unregisterReceiver(receiver)
    }

    /**
     * function to call the Location Service to start
     */
    private fun callService(){
        Log.d("Train Record", "callService: ")
        Intent(this, LocationService::class.java).apply {
            startService(this)
        }
    }

    /**
     * function to stop Service when is no longer needed
     */
    private fun stopService(){
        Intent(this, LocationService::class.java).apply {
            stopService(this)
        }

    }

    private fun addMarker(latitude:Double,longitude:Double){
        val point = LatLng(latitude, longitude)
        mMap.addMarker(MarkerOptions().position(point))
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(point,15f))
    }

    /**
     * function to update current location representation
     *
     * @param latitude of the location
     * @param longitude of the location
     */
    private fun addDot(latitude:Double,longitude:Double){
        headMarker?.remove()

        val point = LatLng(latitude, longitude)
        headMarker = mMap.addMarker(MarkerOptions()
            .icon(BitmapDescriptorFactory.fromResource((R.drawable.blue_dot)))
            .position(point)
            .title("head"))!!
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