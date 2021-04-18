package com.myandroid.sporttracker.ui.track

import android.Manifest
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CalendarView
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.myandroid.sporttracker.R
import com.myandroid.sporttracker.util.TrackingUtil
import pub.devrel.easypermissions.AppSettingsDialog
import pub.devrel.easypermissions.EasyPermissions
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.YearMonth
import java.util.*

class TrackFragment : Fragment(), EasyPermissions.PermissionCallbacks {

    private lateinit var trackViewModel: TrackViewModel

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        trackViewModel =
            ViewModelProvider(this).get(TrackViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_track, container, false)
        val textView: TextView = root.findViewById(R.id.text_dashboard)
        trackViewModel.text.observe(viewLifecycleOwner, Observer {
            textView.text = it
        })


        val calendarView = root.findViewById<CalendarView>(R.id.calendarView)
        calendarView?.setOnDateChangeListener { _, year, month, dayOfMonth ->
            // Note that months are indexed from 0. So, 0 means January, 1 means february, 2 means march etc.
            val msg = "Selected date is " + dayOfMonth + "/" + (month + 1) + "/" + year
            Toast.makeText(this.context, msg, Toast.LENGTH_SHORT).show()
        }

        val current = LocalDateTime.now()
        val day = current.dayOfMonth
        val month = current.monthValue
        val year = current.year
        val yearMonthObject: YearMonth = YearMonth.of(year, month)
        val daysInMonth: Int = yearMonthObject.lengthOfMonth()

        val myDate = "$year/$month/$daysInMonth 23:59:59"
        val sdf = SimpleDateFormat("yyyy/MM/dd HH:mm:ss")
        val date: Date = sdf.parse(myDate)!!
        val millis = date.time

        val yearBefore = year - 1
        val myDate1 = "$yearBefore/$month/$day 00:00:00"
        val sdf1 = SimpleDateFormat("yyyy/MM/dd HH:mm:ss")
        val date1: Date = sdf1.parse(myDate1)!!
        val millis1 = date1.time

        calendarView?.setMinDate(millis1)
        calendarView?.setMaxDate(millis)




        requestPermissions()

        root.findViewById<View>(R.id.fab).setOnClickListener {
            findNavController().navigate(R.id.action_nav_track_to_trackingFragment)
        }
        return root
    }


    private fun requestPermissions() {
        if (TrackingUtil.hasLocationPermissions(requireContext())) {
            return
        }

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
            EasyPermissions.requestPermissions(
                this,
                "You need to accept location permissions to use this app.",
                0,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION
            )
        } else {
            EasyPermissions.requestPermissions(
                this,
                "You need to accept location permissions to use this app.",
                0,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_BACKGROUND_LOCATION
            )
        }
    }

    override fun onPermissionsDenied(requestCode: Int, perms: MutableList<String>) {
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            AppSettingsDialog.Builder(this).build().show()
        } else {
            requestPermissions()
        }
    }

    override fun onPermissionsGranted(requestCode: Int, perms: MutableList<String>) {}

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this)
    }
}