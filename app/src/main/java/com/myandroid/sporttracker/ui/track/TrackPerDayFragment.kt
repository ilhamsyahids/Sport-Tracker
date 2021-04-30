package com.myandroid.sporttracker.ui.track

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.myandroid.sporttracker.R
import com.myandroid.sporttracker.adapters.TrackAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_track_per_day_list.*

/**
 * A fragment representing a list of Items.
 */
@AndroidEntryPoint
class TrackPerDayFragment : Fragment(R.layout.fragment_track_per_day_list) {

    private lateinit var trackAdapter: TrackAdapter
    private val trackViewModel: TrackViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()

        Log.println(Log.ERROR, "TRACK LIST", trackViewModel.allSport.toString())

        trackViewModel.allSport.observe(viewLifecycleOwner, Observer {
            trackAdapter.submitList(it)

        })
    }

    private fun setupRecyclerView() = trackPerDay.apply{
        trackAdapter = TrackAdapter()
        adapter = trackAdapter
        layoutManager = LinearLayoutManager(requireContext())
    }
}