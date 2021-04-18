package com.myandroid.sporttracker.services

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.NotificationManager.IMPORTANCE_LOW
import android.app.PendingIntent
import android.app.PendingIntent.FLAG_UPDATE_CURRENT
import android.content.Context
import android.content.Intent
import android.location.Location
import android.os.Build
import android.os.Looper
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.lifecycle.LifecycleService
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationRequest.PRIORITY_HIGH_ACCURACY
import com.google.android.gms.location.LocationResult
import com.google.android.gms.maps.model.LatLng
import com.myandroid.sporttracker.MainActivity
import com.myandroid.sporttracker.R
import com.myandroid.sporttracker.util.Constant.NOTIFICATION_TRACKING_SERVICE_CHANNEL_ID
import com.myandroid.sporttracker.util.Constant.NOTIFICATION_TRACKING_SERVICE_CHANNEL_NAME
import com.myandroid.sporttracker.util.Constant.NOTIFICATION_TRACKING_SERVICE_ID
import com.myandroid.sporttracker.util.Constant.TRACKING_ACTION_PAUSE_SERVICE
import com.myandroid.sporttracker.util.Constant.TRACKING_ACTION_SHOW_TRACKING_FRAGMENT
import com.myandroid.sporttracker.util.Constant.TRACKING_ACTION_START_OR_RESUME_SERVICE
import com.myandroid.sporttracker.util.Constant.TRACKING_ACTION_STOP_SERVICE
import com.myandroid.sporttracker.util.TrackingUtil

typealias PolyLine = MutableList<LatLng>
typealias PolyLines = MutableList<PolyLine>

class TrackingService: LifecycleService() {

    var isFirstRun = true

    companion object {
        val isTracking = MutableLiveData<Boolean>()
        val pathPoints = MutableLiveData<PolyLines>()
    }

    lateinit var fusedLocationProviderClient: FusedLocationProviderClient

    override fun onCreate() {
        super.onCreate()
        initialValues()
        fusedLocationProviderClient = FusedLocationProviderClient(this)

        isTracking.observe(this, Observer {
            updateLocationTracking(it)
        })
    }

    private fun initialValues() {
        isTracking.postValue(false)
        pathPoints.postValue(mutableListOf())
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        intent?.let {
            when (it.action) {
                TRACKING_ACTION_START_OR_RESUME_SERVICE -> {
                    if (isFirstRun) {
                        startServiceForeground()
                        isFirstRun = false
                    }
                    Log.d("TrackingService", "Started or resume service")
                }
                TRACKING_ACTION_PAUSE_SERVICE -> {
                    Log.d("TrackingService", "Pause service")
                    pauseService()
                }
                TRACKING_ACTION_STOP_SERVICE -> {
                    Log.d("TrackingService", "Stop service")
                }
                else -> {
                    Log.d("TrackingService", "Else service")
                }
            }
        }
        return super.onStartCommand(intent, flags, startId)
    }

    private fun startServiceForeground() {
        addEmptyPolyline()
        isTracking.postValue(true)

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createNotificationChannel(notificationManager)
        }

        val notificationBuilder = NotificationCompat.Builder(this, NOTIFICATION_TRACKING_SERVICE_CHANNEL_ID)
            .setAutoCancel(false)
            .setOngoing(true)
            .setSmallIcon(R.drawable.ic_run)
            .setContentTitle("Sport Tracker")
            .setContentText("00:00:00")
            .setContentIntent(setIntentToMainActivity())

        startForeground(NOTIFICATION_TRACKING_SERVICE_ID, notificationBuilder.build())
    }

    private fun pauseService() {
        isTracking.postValue(false)
    }

    private fun addEmptyPolyline() = pathPoints.value?.apply {
        add(mutableListOf())
        pathPoints.postValue(this)
    } ?: pathPoints.postValue(mutableListOf(mutableListOf()))

    @SuppressLint("MissingPermission")
    private fun updateLocationTracking(isTracking: Boolean) {
        if (isTracking) {
            if (TrackingUtil.hasLocationPermissions(this)) {
                val request = LocationRequest().apply {
                    interval = 4000L
                    fastestInterval = 2000L
                    priority = PRIORITY_HIGH_ACCURACY
                }
                fusedLocationProviderClient.requestLocationUpdates(
                    request,
                    locationCallback,
                    Looper.getMainLooper()
                )
            }
        } else {
            fusedLocationProviderClient.removeLocationUpdates(locationCallback)
        }
    }

    private val locationCallback = object : LocationCallback() {
        override fun onLocationResult(p0: LocationResult?) {
            super.onLocationResult(p0)
            if (isTracking.value!!) {
                p0?.locations?.let { locations ->
                    for (location in locations) {
                        addPathPoint(location)
                        Log.d("LOC", "${location.latitude}, ${location.longitude}")
                    }
                }
            }
        }
    }

    private fun addPathPoint(location: Location?) {
        location?.let {
            val pos = LatLng(location.latitude, location.longitude)
            pathPoints.value?.apply {
                last().add(pos)
                pathPoints.postValue(this)
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotificationChannel(notificationManager: NotificationManager) {
        val channel = NotificationChannel(
            NOTIFICATION_TRACKING_SERVICE_CHANNEL_ID,
            NOTIFICATION_TRACKING_SERVICE_CHANNEL_NAME,
            IMPORTANCE_LOW
        )

        notificationManager.createNotificationChannel(channel)
    }

    private fun setIntentToMainActivity() = PendingIntent.getActivity(
        this,
        0,
        Intent(this, MainActivity::class.java).also {
            it.action = TRACKING_ACTION_SHOW_TRACKING_FRAGMENT
        },
        FLAG_UPDATE_CURRENT
    )
}