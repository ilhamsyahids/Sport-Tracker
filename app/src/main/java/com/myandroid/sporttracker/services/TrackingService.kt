package com.myandroid.sporttracker.services

import android.content.Intent
import android.util.Log
import androidx.lifecycle.LifecycleService
import com.myandroid.sporttracker.util.Constant.TRACKING_ACTION_PAUSE_SERVICE
import com.myandroid.sporttracker.util.Constant.TRACKING_ACTION_START_OR_RESUME_SERVICE
import com.myandroid.sporttracker.util.Constant.TRACKING_ACTION_STOP_SERVICE

class TrackingService: LifecycleService() {

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        intent?.let {
            when (it.action) {
                TRACKING_ACTION_START_OR_RESUME_SERVICE -> {
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
}