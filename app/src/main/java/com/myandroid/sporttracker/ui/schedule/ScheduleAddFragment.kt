package com.myandroid.sporttracker.ui.schedule

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.core.view.get
import androidx.core.view.size
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProvider
import com.myandroid.sporttracker.R
import com.myandroid.sporttracker.db.Frequency
import com.myandroid.sporttracker.db.Reminder
import com.myandroid.sporttracker.ui.dialogs.DatePickerFragment
import com.myandroid.sporttracker.ui.dialogs.TimePickerFragment
import com.nex3z.togglebuttongroup.button.CircularToggle
import kotlinx.android.synthetic.main.fragment_schedule_add.*
import java.text.DateFormatSymbols
import java.util.*

class ScheduleAddFragment : Fragment(), AdapterView.OnItemSelectedListener {

    private lateinit var scheduleViewModel: ScheduleViewModel
    private var mReminder: Reminder? = null

    private var mCustomScheduleDays: java.util.ArrayList<Int?>? = arrayListOf()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        scheduleViewModel =
                ViewModelProvider(this).get(ScheduleViewModel::class.java)

        mReminder = scheduleViewModel.selectedReminder

        return inflater.inflate(R.layout.fragment_schedule_add, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setFrequencySpinnerData()
        setTimePicker()
        setAdditionalDetails()
        setSave()
    }


    private fun setFrequencySpinnerData() {
        val frequencyAdapter = context?.let {
            ArrayAdapter.createFromResource(
                    it, R.array.frequency, android.R.layout.simple_spinner_item)
        }
        frequencyAdapter?.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        frequency_spinner.adapter = frequencyAdapter
        frequency_spinner.onItemSelectedListener = this
        frequency_spinner.setSelection(mReminder?.frequency?.ordinal ?: 0)
    }

    private fun showSelectDaysSpinner() {
        val days = DateFormatSymbols.getInstance(Locale.ENGLISH).shortWeekdays
        select_days_toggle_group.visibility = View.VISIBLE
        select_day_error.visibility = View.VISIBLE

        mReminder?.customDays?.forEach {
            if (it != null && it > 0 && it <= select_days_toggle_group.size) {
                val view = (select_days_toggle_group[it - 1] as CircularToggle)
                select_days_toggle_group.check(view.id)
            }
        }
        select_days_toggle_group.setOnCheckedChangeListener { group, checkedId, isChecked ->
            val text = group.findViewById<CircularToggle>(checkedId).text
            val index = days.indexOf(text)
            if (isChecked)
                mCustomScheduleDays?.add(index)
            else if (!isChecked)
                mCustomScheduleDays?.remove(index)
        }
    }

    private fun setTimePicker() {
        time_picker.setOnClickListener {
            val selectedItem = frequency_spinner.selectedItemPosition
            if (selectedItem == Frequency.Daily.ordinal ||
                    selectedItem == Frequency.Weekly.ordinal) { //Launch the Time picker
                val timePickerFragment: DialogFragment = TimePickerFragment()
                val fm = (activity as FragmentActivity).supportFragmentManager
                Log.d("FM", fm.toString())
                timePickerFragment.show(fm, "timePicker")
            } else { // Launch the date picker first
                val datePickerFragment: DialogFragment = DatePickerFragment()
                val fm = (activity as FragmentActivity).supportFragmentManager
                datePickerFragment.show(fm, "datePicker")
            }
        }
    }

    private fun setAdditionalDetails() {
        more_details_text_view.setOnClickListener {
            // Toggle More details view
            additional_details.visibility =
                    if (additional_details.visibility == View.GONE)
                        View.VISIBLE
                    else View.GONE
        }
    }

    private fun setSave() {
        save_button.setOnClickListener {
            // save
        }
    }

    override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
        when (parent.id) {
            R.id.frequency_spinner -> {
                select_days_toggle_group.visibility = View.GONE
                select_day_error.visibility = View.GONE
                time_picker.setText(R.string.set_time)
                if (position == 2) {
                    showSelectDaysSpinner()
                } else if (position == 3 || position == 0) {
                    time_picker.setText(R.string.set_date_and_time)
                }
            }
        }
    }

    override fun onNothingSelected(p0: AdapterView<*>?) { }
}