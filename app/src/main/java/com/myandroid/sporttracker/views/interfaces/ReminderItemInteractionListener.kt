package com.myandroid.sporttracker.views.interfaces

import android.view.View
import android.widget.CompoundButton
import com.myandroid.sporttracker.db.Reminder

interface ReminderItemInteractionListener {
    fun onToggleButtonClick(buttonView: CompoundButton, isChecked: Boolean, reminder: Reminder?)
}