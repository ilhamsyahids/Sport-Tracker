package com.myandroid.sporttracker.ui.tracking

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.PolylineOptions
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.myandroid.sporttracker.R
import com.myandroid.sporttracker.db.Sport
import com.myandroid.sporttracker.db.SportType
import com.myandroid.sporttracker.services.PolyLine
import com.myandroid.sporttracker.services.TrackingService
import com.myandroid.sporttracker.util.Constant.TRACKING_ACTION_PAUSE_SERVICE
import com.myandroid.sporttracker.util.Constant.TRACKING_ACTION_START_OR_RESUME_SERVICE
import com.myandroid.sporttracker.util.Constant.TRACKING_ACTION_STOP_SERVICE
import com.myandroid.sporttracker.util.TrackingUtil
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_tracking.*
import java.util.*

@AndroidEntryPoint
class TrackingFragment : Fragment(), OnMapReadyCallback {

    companion object {
        fun newInstance() = TrackingFragment()
    }

    private val viewModel: TrackingViewModel by viewModels()

    private var mMap: GoogleMap? = null

    private var isTracking = false
    private var pathPoints = mutableListOf<PolyLine>()
    private var currentTimeInMillis = 0L

    private var menu: Menu? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true)

        return inflater.inflate(R.layout.fragment_tracking, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mapView.onCreate(savedInstanceState)

        mapView.getMapAsync(this)

        btnToggleTracking.setOnClickListener {
            toggleTracking()
        }
        
        btnFinishTrack.setOnClickListener {
            viewAllTrack()
            finishTrackAndSaveToDb()
        }

        subscribeToObservers()
    }

    private fun viewAllTrack() {
        if (pathPoints.isNotEmpty()) {
            val bounds = LatLngBounds.Builder()
            for (pl in pathPoints) {
                for (pos in pl) {
                    bounds.include(pos)
                }
            }

            mMap?.moveCamera(
                    CameraUpdateFactory.newLatLngBounds(
                            bounds.build(),
                            mapView.width,
                            mapView.height,
                            (mapView.height * 0.1f).toInt()
                    )
            )
        }
    }

    private fun finishTrackAndSaveToDb() {
        mMap?.snapshot { bmp ->
            var distanceInMeters = 0
            for (polyline in pathPoints) {
                distanceInMeters += TrackingUtil.calcPolylineLength(polyline).toInt()
            }
            val dateTimestamp = Calendar.getInstance().timeInMillis
            val sport = Sport(bmp, dateTimestamp, distanceInMeters, currentTimeInMillis, SportType.CYCLING)

            viewModel.insertSport(sport)
            Toast.makeText(
                    this.context,
                    "Saved successfully",
                    Toast.LENGTH_LONG)
                    .show()
            stopTrack()
        }
    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.top_tracking_menu, menu)
        this.menu = menu
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        super.onPrepareOptionsMenu(menu)
        if (currentTimeInMillis > 0L) {
            this.menu?.getItem(0)?.isVisible = true
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.cancel_tracking -> {
                showCancelTrackDialog()
            }
        }
        return super.onOptionsItemSelected(item)
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
            Log.d("isTracking.observe", it.toString())
            if (it != -1) updateBtnTracking(it == 1)
        })

        TrackingService.pathPoints.observe(viewLifecycleOwner, {
            pathPoints = it
            addLatestPolyline()
            moveCameraToUser()
        })

        TrackingService.timeRunInMillis.observe(viewLifecycleOwner, {
            currentTimeInMillis = it
            val formatted = TrackingUtil.getFormattedStopWatchTime(currentTimeInMillis, true)
            tvTimer.text = formatted
        })
    }

    private fun toggleTracking() {
        if (isTracking) {
            menu?.getItem(0)?.isVisible = true
            sendCommandToTrackingService(TRACKING_ACTION_PAUSE_SERVICE)
        } else {
            sendCommandToTrackingService(TRACKING_ACTION_START_OR_RESUME_SERVICE)
        }
    }

    private fun updateBtnTracking(isTracking: Boolean) {
        this.isTracking = isTracking
        if (!isTracking) {
            btnToggleTracking.setText(R.string.resume)
            btnFinishTrack.visibility = View.VISIBLE
        } else {
            btnToggleTracking.setText(R.string.stop)
            menu?.getItem(0)?.isVisible = true
            btnFinishTrack.visibility = View.GONE
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

    private fun showCancelTrackDialog() {
        val dialog = MaterialAlertDialogBuilder(requireContext())
            .setTitle("Cancel The Run?")
            .setMessage("Are you sure to cancel the current run and delete all its data?")
            .setPositiveButton("Yes") { _, _ ->
                stopTrack()
            }
            .setNegativeButton("No") { dialogInterface, _ ->
                dialogInterface.cancel()
            }
            .create()
        dialog.show()
    }

    private fun stopTrack() {
        sendCommandToTrackingService(TRACKING_ACTION_PAUSE_SERVICE)
        sendCommandToTrackingService(TRACKING_ACTION_STOP_SERVICE)
        findNavController().navigate(R.id.action_trackingFragment_to_nav_track)
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