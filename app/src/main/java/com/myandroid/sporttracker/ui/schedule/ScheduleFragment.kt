package com.myandroid.sporttracker.ui.schedule

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.CompoundButton
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.myandroid.sporttracker.R
import com.myandroid.sporttracker.adapters.ReminderAdapter
import com.myandroid.sporttracker.db.Reminder
import com.myandroid.sporttracker.ui.tracking.TrackingViewModel
import com.myandroid.sporttracker.views.interfaces.ReminderItemInteractionListener
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_schedule.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.ArrayList

@AndroidEntryPoint
class ScheduleFragment : Fragment(), ReminderItemInteractionListener {

    private val viewModel: ScheduleViewModel by viewModels()

    private lateinit var reminderAdapter: ReminderAdapter
    private var isRescheduleAtLaunch = true

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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()

        viewModel.reminderByDate.observe(viewLifecycleOwner, {
            reminderAdapter.submitList(it)
        })
    }

    private fun setupRecyclerView() {
        val self = this
        reminder_list.apply {
            reminderAdapter = ReminderAdapter()
            reminderAdapter.setOnItemClickListener(self)
            adapter = reminderAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
    }

    override fun onToggleButtonClick(buttonView: CompoundButton, isChecked: Boolean, reminder: Reminder?) {
        if (reminder != null &&(buttonView.isPressed || isRescheduleAtLaunch)) {
            if (isChecked) {
                viewModel.scheduleReminder(reminder)
            }
            else {
                viewModel.cancelScheduledReminder(reminder)
            }
        }
        isRescheduleAtLaunch = false
    }
}