package com.myandroid.sporttracker.ui.track

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.myandroid.sporttracker.R

class TrackFragment : Fragment() {

    private lateinit var trackViewModel: TrackViewModel

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

        root.findViewById<View>(R.id.fab).setOnClickListener {
            findNavController().navigate(R.id.action_nav_track_to_trackingFragment)
        }
        return root
    }
}