package com.myandroid.sporttracker.sensors

import android.app.Activity
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import com.myandroid.sporttracker.ui.tracking.TrackingFragment

class Compass(context: TrackingFragment) : SensorEventListener {

    private var sensorManager: SensorManager = context.getSystemService(Activity.SENSOR_SERVICE) as SensorManager
    private var listener: CompassListener? = null
    private var mSensor: Sensor = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD)
    private var gSensor: Sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)

    private var azimuth = 0f
    private var azimuthFix = 0f

    private val mGravity = FloatArray(3)
    private val mGeomagnetic = FloatArray(3)
    private val mR = FloatArray(9)
    private val mI = FloatArray(9)

    interface CompassListener {
        fun onNewAzimuth(azimuth: Float)
    }

    fun start() {
        sensorManager.registerListener(this, gSensor, SensorManager.SENSOR_DELAY_GAME)
        sensorManager.registerListener(this, mSensor, SensorManager.SENSOR_DELAY_GAME)
    }

    fun stop() {
        sensorManager.unregisterListener(this)
    }

    fun setListener(l: CompassListener) {
        listener = l
    }

    override fun onSensorChanged(event: SensorEvent) {
        val alpha = 0.97f
        synchronized(this) {
            if (event.sensor.type == Sensor.TYPE_ACCELEROMETER) {
                mGravity[0] = alpha * mGravity[0] + (1 - alpha) * event.values[0]
                mGravity[1] = alpha * mGravity[1] + (1 - alpha) * event.values[1]
                mGravity[2] = alpha * mGravity[2] + (1 - alpha) * event.values[2]
            }
            if (event.sensor.type == Sensor.TYPE_MAGNETIC_FIELD) {
                mGeomagnetic[0] = alpha * mGeomagnetic[0] + (1 - alpha) * event.values[0]
                mGeomagnetic[1] = alpha * mGeomagnetic[1] + (1 - alpha) * event.values[1]
                mGeomagnetic[2] = alpha * mGeomagnetic[2] + (1 - alpha) * event.values[2]
            }
            val success = SensorManager.getRotationMatrix(mR, mI, mGravity, mGeomagnetic)
            if (success) {
                val orientation = FloatArray(3)
                SensorManager.getOrientation(mR, orientation)
                azimuth = Math.toDegrees(orientation[0].toDouble()).toFloat()
                azimuth = (azimuth + azimuthFix + 360) % 360
                listener?.onNewAzimuth(azimuth)
            }
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}
}