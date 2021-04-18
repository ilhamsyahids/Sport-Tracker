package com.myandroid.sporttracker.ui.tracking

import android.content.Intent
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.myandroid.sporttracker.R
import com.myandroid.sporttracker.services.TrackingService
import com.myandroid.sporttracker.util.Constant.TRACKING_ACTION_START_OR_RESUME_SERVICE
import kotlinx.android.synthetic.main.fragment_tracking.*

class TrackingFragment : Fragment(), OnMapReadyCallback {

    companion object {
        fun newInstance() = TrackingFragment()
    }

    private lateinit var viewModel: TrackingViewModel

    private lateinit var mMap: GoogleMap

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

        btnStart.setOnClickListener {
            sendCommandToTrackingService(TRACKING_ACTION_START_OR_RESUME_SERVICE)
        }
    }

    private fun sendCommandToTrackingService(action: String) = Intent(requireContext(), TrackingService::class.java).also {
        it.action = action
        requireContext().startService(it)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        // Add a marker in ITB and move the camera
        val itb = LatLng(-6.891192, 107.610447)
        mMap.addMarker(MarkerOptions().position(itb).title("Marker in ITB"))
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(itb, 16f))
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