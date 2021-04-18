package com.myandroid.sporttracker.ui.tracking

import android.content.Intent
import android.graphics.Color
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.PolylineOptions
import com.myandroid.sporttracker.R
import com.myandroid.sporttracker.services.PolyLine
import com.myandroid.sporttracker.services.TrackingService
import com.myandroid.sporttracker.util.Constant.TRACKING_ACTION_PAUSE_SERVICE
import com.myandroid.sporttracker.util.Constant.TRACKING_ACTION_START_OR_RESUME_SERVICE
import kotlinx.android.synthetic.main.fragment_tracking.*

class TrackingFragment : Fragment(), OnMapReadyCallback {

    companion object {
        fun newInstance() = TrackingFragment()
    }

    private lateinit var viewModel: TrackingViewModel

    private var mMap: GoogleMap? = null

    private var isTracking = false
    private var pathPoints = mutableListOf<PolyLine>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_tracking, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(TrackingViewModel::class.java)
        // TODO: Use the ViewModel
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mapView.onCreate(savedInstanceState)

        mapView.getMapAsync(this)

        btnToggleTracking.setOnClickListener {
            toggleTracking()
        }

        subscribeToObservers()
    }

    private fun sendCommandToTrackingService(action: String) = Intent(requireContext(), TrackingService::class.java).also {
        it.action = action
        requireContext().startService(it)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        addAllPolylines()
    }

    private fun subscribeToObservers() {
        TrackingService.isTracking.observe(viewLifecycleOwner, {
            updateBtnTracking(it)
        })

        TrackingService.pathPoints.observe(viewLifecycleOwner, {
            pathPoints = it
            addLatestPolyline()
            moveCameraToUser()
        })
    }

    private fun toggleTracking() {
        if (isTracking) {
            sendCommandToTrackingService(TRACKING_ACTION_PAUSE_SERVICE)
        } else {
            sendCommandToTrackingService(TRACKING_ACTION_START_OR_RESUME_SERVICE)
        }
    }

    private fun updateBtnTracking(isTracking: Boolean) {
        this.isTracking = isTracking
        if (!isTracking) {
            btnToggleTracking.text = "Start"
            btnFinishRun.visibility = View.VISIBLE
        } else {
            btnToggleTracking.text = "Stop"
            btnFinishRun.visibility = View.GONE
        }
    }

    private fun moveCameraToUser() {
        if (pathPoints.isNotEmpty() && pathPoints.last().isNotEmpty()) {
            mMap?.animateCamera(
                CameraUpdateFactory.newLatLngZoom(
                    pathPoints.last().last(),
                    15f
                )
            )
        }
    }

    private fun addLatestPolyline() {
        if (pathPoints.isNotEmpty() && pathPoints.last().size > 1) {
            val preLastLatLng = pathPoints.last()[pathPoints.last().size - 2]
            val lastLatLng = pathPoints.last().last()
            val polylineOptions = PolylineOptions()
                .color(Color.BLUE)
                .width(8f)
                .add(preLastLatLng)
                .add(lastLatLng)
            mMap?.addPolyline(polylineOptions)
        }
    }

    private fun addAllPolylines() {
        for (polyline in pathPoints) {
            val polylineOptions = PolylineOptions()
                .color(Color.BLUE)
                .width(8f)
                .addAll(polyline)
            mMap?.addPolyline(polylineOptions)
        }
    }

    override fun onResume() {
        super.onResume()

        mapView?.onResume()
    }

    override fun onPause() {
        super.onPause()

        mapView?.onPause()
    }

    override fun onStart() {
        super.onStart()

        mapView?.onStart()
    }

    override fun onStop() {
        super.onStop()

        mapView?.onStop()
    }

    override fun onLowMemory() {
        super.onLowMemory()

        mapView?.onLowMemory()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        mapView?.onSaveInstanceState(outState)
    }
}