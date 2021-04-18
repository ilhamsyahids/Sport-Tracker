package com.myandroid.sporttracker.services

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.NotificationManager.IMPORTANCE_LOW
import android.app.PendingIntent
import android.app.PendingIntent.FLAG_UPDATE_CURRENT
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.lifecycle.LifecycleService
import com.myandroid.sporttracker.MainActivity
import com.myandroid.sporttracker.R
import com.myandroid.sporttracker.util.Constant.NOTIFICATION_TRACKING_SERVICE_CHANNEL_ID
import com.myandroid.sporttracker.util.Constant.NOTIFICATION_TRACKING_SERVICE_CHANNEL_NAME
import com.myandroid.sporttracker.util.Constant.NOTIFICATION_TRACKING_SERVICE_ID
import com.myandroid.sporttracker.util.Constant.TRACKING_ACTION_PAUSE_SERVICE
import com.myandroid.sporttracker.util.Constant.TRACKING_ACTION_SHOW_TRACKING_FRAGMENT
import com.myandroid.sporttracker.util.Constant.TRACKING_ACTION_START_OR_RESUME_SERVICE
import com.myandroid.sporttracker.util.Constant.TRACKING_ACTION_STOP_SERVICE

class TrackingService: LifecycleService() {

    var isFirstRun = true

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