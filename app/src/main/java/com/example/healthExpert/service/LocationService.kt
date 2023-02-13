package com.example.healthExpert.service

import android.Manifest
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.PendingIntent.FLAG_IMMUTABLE
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.os.Build
import android.os.Looper
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleService
import com.example.healthExpert.view.training.TrainRecord
import com.google.android.gms.location.*


/**
 * This is a Service class to implement the Foreground Service for keep tracking location
 *
 * @return This function returns a service to get location from GPS sensor.
 */
class LocationService : LifecycleService() {
    private val CHANNEL_ID = "notification channel id"
    private lateinit var lastLocation: Location
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var locationCallback: LocationCallback

    /**
     * onCreate lifecycle to create a Foreground Service
     */
    override fun onCreate() {
        Log.d("Service", "onCreate: ")
        super.onCreate()
        createChannel()
        val pendingIntent = createPendingIntent()
        val notification = pendingIntent?.let { createNotification(it) }
        startForeground(1, notification)
    }

    /**
     * onStartCommand lifecycle to start the Service
     *
     * @param intent contains the data passed to the service when it was started
     * @param flags  an integer value that used to provide additional information about how the service was started.
     * @param startId an integer value that uniquely identifies the start request for the service
     *
     * @return an integer value indicating to the system how it should continue executing the Service
     */
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.d("Service", "onStartCommand: ")
        locationCallback = object : LocationCallback() {
            override fun onLocationResult(p0: LocationResult) {
                p0?:return
                for (location in p0.locations){
                    lastLocation = location
                    val intent = Intent("update-ui")
                    intent.putExtra("latitude", lastLocation.latitude.toString())
                    intent.putExtra("longitude", lastLocation.longitude.toString())
                    sendBroadcast(intent)
                    Log.d("service", "lastLocation: ${location.latitude},${location.longitude} ")
                }
            }
        }
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            var locationRequest = LocationRequest.Builder(
                Priority.PRIORITY_HIGH_ACCURACY,
                10000
            ).build()
            fusedLocationClient.requestLocationUpdates(locationRequest,locationCallback, Looper.getMainLooper())
        }
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onDestroy() {
        Log.d("Service", "onDestroy: ")
        super.onDestroy()
        stopSelf()
        fusedLocationClient.removeLocationUpdates(locationCallback)
    }

    /**
     * A function to Create Pending Intent for creating notification for Foreground Service
     *
     * @return a pendingIntent object
     */
    private fun createPendingIntent(): PendingIntent? {
        Log.d("Service", "createPendingIntent: ")
        val pendingIntent = PendingIntent.getActivity(
            this,
            0,
            Intent(this, TrainRecord::class.java),
            FLAG_IMMUTABLE
        )
        return pendingIntent
    }

    /**
     * A function to Create notification for Foreground Service
     *
     * @param pendingIntent
     *
     * @return a pendingIntent object
     */
    private fun createNotification(pendingIntent: PendingIntent): Notification {
        Log.d("Service", "createNotification: ")
        val notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("We are keep tracking your location...")
            .setContentText("Click here to come back")
            .setContentIntent(pendingIntent)
            .build()
        return notification
    }

    /**
     * A function to Create the NotificationChannel
     */
    private fun createChannel() {
        Log.d("Service", "createChannel: ")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // Create the NotificationChannel
            val name = "Notification Channel Name"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val mChannel = NotificationChannel(CHANNEL_ID, name, importance)
            val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(mChannel)
        }
    }
}
