package com.myandroid.sporttracker.util

import android.Manifest
import android.content.Context
import android.location.Location
import android.os.Build
import com.myandroid.sporttracker.services.PolyLine
import pub.devrel.easypermissions.EasyPermissions
import java.util.concurrent.TimeUnit

object TrackingUtil {

    fun hasLocationPermissions(context: Context) =
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
            EasyPermissions.hasPermissions(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            )
        } else {
            EasyPermissions.hasPermissions(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_BACKGROUND_LOCATION
            )
        }


    fun getFormattedStopWatchTime(ms: Long, includeMillis: Boolean = false): String {
        var millis: Long = ms
        val hours = TimeUnit.MILLISECONDS.toHours(millis)
        millis -= TimeUnit.HOURS.toMillis(hours)

        val minutes = TimeUnit.MILLISECONDS.toMinutes(millis)
        millis -= TimeUnit.MINUTES.toMillis(minutes)

        val sec = TimeUnit.MILLISECONDS.toSeconds(millis)

        var str = "${if (hours < 10) "0" else ""}$hours:" +
                "${if (minutes < 10) "0" else ""}$minutes:" +
                "${if (sec < 10) "0" else ""}$sec"

        if(includeMillis) {
            millis -= TimeUnit.SECONDS.toMillis(sec)
            millis /= 10

            str += ":${if (millis < 10) "0" else ""}$millis"
        }

        return str
    }


    fun calcPolylineLength(polyLine: PolyLine): Float {
        var distance = 0f
        for (i in 0..polyLine.size - 2) {
            val pos1 = polyLine[i]
            val pos2 = polyLine[i + 1]

            val result = FloatArray(1)
            Location.distanceBetween(pos1.latitude, pos1.longitude, pos2.latitude, pos2.longitude, result)

            distance += result[0]
        }

        return distance
    }
}