package com.myandroid.sporttracker.ui.track

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.myandroid.sporttracker.R
import com.myandroid.sporttracker.adapters.TrackAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_track_per_day_list.*

@AndroidEntryPoint
class TrackDetailsFragment : Fragment(R.layout.fragment_track_per_day_list) {

    private lateinit var trackAdapter: TrackAdapter
    private val trackViewModel: TrackViewModel by viewModels()

//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//        setupRecyclerView()
//
//        trackViewModel.allSport.observe(viewLifecycleOwner, Observer {
//            trackAdapter.submitList(it)
//
//        })
//    }
}