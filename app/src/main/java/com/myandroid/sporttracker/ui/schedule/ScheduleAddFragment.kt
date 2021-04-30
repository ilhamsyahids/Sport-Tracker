package com.myandroid.sporttracker.ui.schedule

import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.myandroid.sporttracker.R
import com.myandroid.sporttracker.db.Frequency
import com.myandroid.sporttracker.db.Reminder
import com.myandroid.sporttracker.db.SportType
import com.myandroid.sporttracker.ui.dialogs.DatePickerFragment
import com.myandroid.sporttracker.ui.dialogs.TimePickerFragment
import com.myandroid.sporttracker.util.TimeDateUtil.getTimestampInSeconds
import com.nex3z.togglebuttongroup.button.CircularToggle
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_schedule_add.*
import java.text.DateFormatSymbols
import java.util.*

@AndroidEntryPoint
class ScheduleAddFragment : Fragment(), AdapterView.OnItemSelectedListener {

    private val viewModel: ScheduleViewModel by viewModels()
//    private var mReminder: Reminder? = null

    private var mCustomScheduleDays: ArrayList<Int?>? = arrayListOf()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
//        mReminder = viewModel.selectedReminder

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
        frequency_spinner.setSelection(0)
//        frequency_spinner.setSelection(mReminder?.frequency?.ordinal ?: 0)
    }

    private fun showSelectDaysSpinner() {
        val days = DateFormatSymbols.getInstance(Locale.ENGLISH).shortWeekdays
        select_days_toggle_group.visibility = View.VISIBLE
        select_day_error.visibility = View.VISIBLE

//        mReminder?.customDays?.forEach {
//            if (it != null && it > 0 && it <= select_days_toggle_group.size) {
//                val view = (select_days_toggle_group[it - 1] as CircularToggle)
//                select_days_toggle_group.check(view.id)
//            }
//        }
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
            validateAndSave()
        }
    }

    private fun validateAndSave() {
        if (TextUtils.isEmpty(reminder_edit_text.text.toString().trim { it <= ' ' })) {
            Toast.makeText(context, getString(R.string.fill_the_name), Toast.LENGTH_SHORT).show()
            return
        }

        val selection = frequency_spinner.selectedItemPosition
        var day = 0
        var month = 12
        var year = 0
        val timeSet = TimePickerFragment.timeSet.value

        when (selection) {
            Frequency.Weekly.ordinal -> {
                day = -1
                if (mCustomScheduleDays.isNullOrEmpty()) {
                    //error
                    Toast.makeText(context, getString(R.string.fill_atleast_one_day), Toast.LENGTH_LONG).show()
                    select_day_error.visibility = View.VISIBLE
                    return
                }
            }
            Frequency.Monthly.ordinal -> {
                if (DatePickerFragment.mDay == 0) {
                    Toast.makeText(context, R.string.fill_pick_date_and_time, Toast.LENGTH_LONG).show()
                    return
                }
                day = DatePickerFragment.mDay
            }
            Frequency.OneTime.ordinal -> {
                if (DatePickerFragment.mDay == 0 ||
                        DatePickerFragment.mMonth == 12 || DatePickerFragment.mYear == 0 ||
                        timeSet?.matches(getString(R.string.time_not_set).toRegex()) == true) {
                    Toast.makeText(context, R.string.fill_pick_date_and_time, Toast.LENGTH_LONG).show()
                    return
                }
                day = DatePickerFragment.mDay
                month = DatePickerFragment.mMonth
                year = DatePickerFragment.mYear
            }
        }
        val reminder = createNewReminder(timeSet, Frequency.values()[selection], day, month, year, mCustomScheduleDays)
        saveReminder(reminder)
    }


    private fun createNewReminder(timeSet: String?, frequency: Frequency, day: Int, month: Int, year: Int, customScheduleDays: ArrayList<Int?>?): Reminder {
        val sportType = SportType.valueOf(category_spinner.selectedItem.toString().toUpperCase(Locale.getDefault()))

        return Reminder(
                reminder_edit_text.text.toString(),
                additional_details.text.toString(),
                getTimestampInSeconds(timeSet),
                sportType,
                frequency,
                timeSet?.matches(getString(R.string.time_not_set).toRegex()) == false,
                day,
                month,
                year,
                if (frequency == Frequency.Weekly && customScheduleDays != null) customScheduleDays else arrayListOf()
        )
    }

    private fun saveReminder(reminder: Reminder) {
        viewModel.insertReminder(reminder)
        Toast.makeText(context, getString(R.string.successfuly_added), Toast.LENGTH_LONG).show()
        findNavController().navigate(R.id.action_scheduleAddFragment_to_nav_schedule)
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