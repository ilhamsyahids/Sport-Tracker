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
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationRequest.PRIORITY_HIGH_ACCURACY
import com.google.android.gms.location.LocationResult
import com.google.android.gms.maps.model.LatLng
import com.myandroid.sporttracker.MainActivity
import com.myandroid.sporttracker.R
import com.myandroid.sporttracker.db.SportType
import com.myandroid.sporttracker.util.Constant.NOTIFICATION_TRACKING_SERVICE_CHANNEL_ID
import com.myandroid.sporttracker.util.Constant.NOTIFICATION_TRACKING_SERVICE_CHANNEL_NAME
import com.myandroid.sporttracker.util.Constant.NOTIFICATION_TRACKING_SERVICE_ID
import com.myandroid.sporttracker.util.Constant.TRACKING_ACTION_PAUSE_SERVICE
import com.myandroid.sporttracker.util.Constant.TRACKING_ACTION_SHOW_TRACKING_FRAGMENT
import com.myandroid.sporttracker.util.Constant.TRACKING_ACTION_START_OR_RESUME_SERVICE
import com.myandroid.sporttracker.util.Constant.TRACKING_ACTION_STOP_SERVICE
import com.myandroid.sporttracker.util.TrackingUtil
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

typealias PolyLine = MutableList<LatLng>
typealias PolyLines = MutableList<PolyLine>

@AndroidEntryPoint
class TrackingService: LifecycleService() {

    companion object {
        val isTracking = MutableLiveData<Int>()
        val pathPoints = MutableLiveData<PolyLines>()

        val sportType = MutableLiveData<SportType>()

        val timeRunInMillis = MutableLiveData<Long>()
        val timeRunInSeconds = MutableLiveData<Long>()
    }

    @Inject
    lateinit var fusedLocationProviderClient: FusedLocationProviderClient

    @Inject
    lateinit var baseNotificationBuilder: NotificationCompat.Builder

    private lateinit var currNotificationBuilder: NotificationCompat.Builder

    private var isFirstRun = true
    private var killedService = false

    private var isTimerEnabled = false
    private var lapTime = 0L
    private var timeRun = 0L
    private var timeStarted = 0L
    private var lastSecondTimestamp = 0L

    override fun onCreate() {
        super.onCreate()
        initialValues()
        currNotificationBuilder = baseNotificationBuilder
        fusedLocationProviderClient = FusedLocationProviderClient(this)

        isTracking.observe(this, {
            updateLocationTracking(it == 1)
            updateNotificationTrackingState(it == 1)
        })
    }

    private fun initialValues() {
        isTracking.postValue(-1)
        pathPoints.postValue(mutableListOf())
        timeRunInSeconds.postValue(0L)
        timeRunInMillis.postValue(0L)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        intent?.let {
            when (it.action) {
                TRACKING_ACTION_START_OR_RESUME_SERVICE -> {
                    if (isFirstRun) {
                        Log.d("TrackingService", "Start service")
                        startServiceForeground()
                        isFirstRun = false
                    } else {
                        Log.d("TrackingService", "Resume service")
                        startTimer()
                    }
                }
                TRACKING_ACTION_PAUSE_SERVICE -> {
                    Log.d("TrackingService", "Pause service")
                    pauseService()
                }
                TRACKING_ACTION_STOP_SERVICE -> {
                    Log.d("TrackingService", "Stop service")
                    killService()
                }
                else -> {
                    Log.d("TrackingService", "Else service")
                }
            }
        }
        return super.onStartCommand(intent, flags, startId)
    }

    private fun startServiceForeground() {
        startTimer()
        isTracking.postValue(1)

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createNotificationChannel(notificationManager)
        }

        startForeground(NOTIFICATION_TRACKING_SERVICE_ID, baseNotificationBuilder.build())


        timeRunInSeconds.observe(this, {
            if (!killedService) {
                val notif = currNotificationBuilder
                    .setContentText(TrackingUtil.getFormattedStopWatchTime(it * 1000L))
                notificationManager.notify(NOTIFICATION_TRACKING_SERVICE_ID, notif.build())
            }
        })
    }

    private fun killService() {
        killedService = true
        isFirstRun = true
        pauseService()
        initialValues()
        stopForeground(true)
        stopSelf()
    }

    private fun startTimer() {
        addEmptyPolyline()
        isTracking.postValue(1)
        timeStarted = System.currentTimeMillis()
        isTimerEnabled = true

        if (sportType.value == SportType.RUNNING) {
            Log.d("SENSOR", "Start and resume sensor step")
        }
        CoroutineScope(Dispatchers.Main).launch {
            while(isTracking.value!! == 1) {
                // time diff between now and time started
                lapTime = System.currentTimeMillis() - timeStarted

                timeRunInMillis.postValue(timeRun + lapTime)

                if (timeRunInMillis.value!! >= lastSecondTimestamp + 1000L) {
                    timeRunInSeconds.postValue(timeRunInSeconds.value!! + 1)
                    lastSecondTimestamp += 1000L
                }
                delay(50L)
            }
            timeRun += lapTime
        }
    }

    private fun pauseService() {
        isTracking.postValue(0)
        isTimerEnabled = false

        Log.d("Service Sport Type", sportType.value.toString())

        if (sportType.value == SportType.RUNNING) {
            Log.d("Service Sport Type", "TODO")
            Log.d("SENSOR", "Pause sensor step")
        }
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
            if (isTracking.value!! == 1) {
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

    private fun updateNotificationTrackingState(isTracking: Boolean) {
        val notificationActionText = if (isTracking) "Pause" else "Resume"
        val pendingIntent = if (isTracking) {
            val pauseIntent = Intent(this, TrackingService::class.java).apply {
                action = TRACKING_ACTION_PAUSE_SERVICE
            }
            PendingIntent.getService(this, 1, pauseIntent, FLAG_UPDATE_CURRENT)
        } else {
            val resumeIntent = Intent(this, TrackingService::class.java).apply {
                action = TRACKING_ACTION_START_OR_RESUME_SERVICE
            }
            PendingIntent.getService(this, 2, resumeIntent, FLAG_UPDATE_CURRENT)
        }

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        currNotificationBuilder.javaClass.getDeclaredField("mActions").apply {
            isAccessible = true
            set(currNotificationBuilder, ArrayList<NotificationCompat.Action>())
        }
        if (!killedService) {
            currNotificationBuilder = baseNotificationBuilder
                .addAction(R.drawable.ic_pause_black_24dp, notificationActionText, pendingIntent)
            notificationManager.notify(NOTIFICATION_TRACKING_SERVICE_ID, currNotificationBuilder.build())
        }
    }
}