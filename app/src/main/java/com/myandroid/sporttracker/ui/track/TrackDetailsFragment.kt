package com.myandroid.sporttracker.ui.track

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.myandroid.sporttracker.R
import com.myandroid.sporttracker.adapters.TrackAdapter
import com.myandroid.sporttracker.db.SportType
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_track_details.*
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

@AndroidEntryPoint
class TrackDetailsFragment : Fragment(R.layout.fragment_track_details) {

    private lateinit var trackAdapter: TrackAdapter
    private val trackViewModel: TrackViewModel by viewModels()

    private val args: TrackDetailsFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val track = args.sport

        Glide.with(this).load(track.image).into(trackImg)

        sport_type.text = track.type.toString()

        if (track.type == SportType.RUNNING) {
            steps.visibility = View.VISIBLE
            steps.text = track.steps.toString() + " step(s)"
        }

        val sdf = SimpleDateFormat("dd/MM/yy hh:mm:ss")
        val netDate = Date(track.timestamp)
        val date = sdf.format(netDate)
        timestamp.text = date

        distance.text = track.distanceInMeters.toString() + " meters"

        duration.text = String.format("%02d hr %02d min, %02d sec",
                TimeUnit.MILLISECONDS.toHours(track.timeInMillis),
                TimeUnit.MILLISECONDS.toMinutes(track.timeInMillis) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(track.timeInMillis)),
                TimeUnit.MILLISECONDS.toSeconds(track.timeInMillis) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(track.timeInMillis)))
    }
}