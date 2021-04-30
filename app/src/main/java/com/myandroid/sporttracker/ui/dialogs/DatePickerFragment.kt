package com.myandroid.sporttracker.ui.dialogs

import android.app.DatePickerDialog
import android.app.Dialog
import android.os.Bundle
import android.widget.DatePicker
import androidx.fragment.app.DialogFragment
import java.util.*

class DatePickerFragment : DialogFragment(), DatePickerDialog.OnDateSetListener {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        // Use the current date as the default date in the picker
        val c = Calendar.getInstance()
        var year = c[Calendar.YEAR]
        var month = c[Calendar.MONTH]
        var day = c[Calendar.DAY_OF_MONTH]

        // Conditions for when the reminder already has a set date
        if (mDay > 0)
            day = mDay
        if (mMonth != 12)
            month = mMonth
        if (mYear != 0)
            year = mYear

        // Create a new instance of DatePickerDialog and return it
        return DatePickerDialog(requireContext(), this, year, month, day)
    }

    override fun onDateSet(view: DatePicker, year: Int, month: Int, day: Int) {
        mDay = day
        mMonth = month
        mYear = year
        val timePickerFragment: DialogFragment = TimePickerFragment()
        val fm = requireActivity().supportFragmentManager
        timePickerFragment.show(fm, "timePicker")
    }

    companion object {
        var mDay = 0
        var mMonth = 12
        var mYear = 0
    }
}