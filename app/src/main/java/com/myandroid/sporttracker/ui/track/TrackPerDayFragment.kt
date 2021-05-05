package com.myandroid.sporttracker.ui.track

import android.content.res.Configuration
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.myandroid.sporttracker.R
import com.myandroid.sporttracker.adapters.TrackAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_track_list.*
import kotlinx.android.synthetic.main.fragment_track_per_day_list.*


/**
 * A fragment representing a list of Items.
 */
@AndroidEntryPoint
class TrackPerDayFragment : Fragment(R.layout.fragment_track_list) {

    private lateinit var trackAdapter: TrackAdapter
    private val trackViewModel: TrackViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()

        trackAdapter.setOnItemClickListener {
            val bundle = Bundle().apply {
                putSerializable("sport", it)
            }
            if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
                //Do some stuff
                    val details = TrackDetailsFragment()
                val transaction = childFragmentManager.beginTransaction()
                transaction.add(R.id.trackDetails, details.javaClass, bundle).commit()
            }else{
                findNavController().navigate(
                    R.id.action_trackPerDayFragment_to_trackDetails,
                    bundle
                )
            }

        }

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