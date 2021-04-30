package com.myandroid.sporttracker.ui.schedule

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.myandroid.sporttracker.R
import com.myandroid.sporttracker.ui.tracking.TrackingViewModel

class ScheduleFragment : Fragment() {

    private val viewModel: ScheduleViewModel by viewModels()

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_schedule, container, false)

        root.findViewById<View>(R.id.fabSchedule).setOnClickListener {
            findNavController().navigate(R.id.action_nav_schedule_to_scheduleAddFragment)
        }

        return root
    }
}