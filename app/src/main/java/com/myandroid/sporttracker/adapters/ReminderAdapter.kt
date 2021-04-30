package com.myandroid.sporttracker.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.myandroid.sporttracker.R
import com.myandroid.sporttracker.db.Reminder
import com.myandroid.sporttracker.util.TimeDateUtil
import com.myandroid.sporttracker.views.interfaces.ReminderItemInteractionListener
import kotlinx.android.synthetic.main.item_reminder.view.*

class ReminderAdapter: RecyclerView.Adapter<ReminderAdapter.ReminderViewHolder>() {

    inner class ReminderViewHolder(itemView: View): RecyclerView.ViewHolder(itemView)

    val differCallback = object : DiffUtil.ItemCallback<Reminder>() {
        override fun areItemsTheSame(oldItem: Reminder, newItem: Reminder): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Reminder, newItem: Reminder): Boolean {
            return oldItem.hashCode() == newItem.hashCode()
        }
    }

    private val differ = AsyncListDiffer(this, differCallback)

    fun submitList(list: List<Reminder>) = differ.submitList(list)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReminderViewHolder {
        return ReminderViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_reminder,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ReminderViewHolder, position: Int) {
        val reminder = differ.currentList[position]
        holder.itemView.apply {
            reminder_title.text = reminder.name
            reminder_category.text = reminder.type?.name
            toggleButton.isChecked = reminder.isEnabled
            if (reminder.frequency?.ordinal == 0) {
                reminder_when.text =
                    if (reminder.month!! < 12)
                        String.format(
                            resources.getString(R.string.one_time_frequency_display),
                            reminder.day,
                            TimeDateUtil.getMonthName(reminder.month!!),
                            reminder.year) +
                                String.format(
                                    resources.getString(R.string.at_time),
                                    TimeDateUtil.formatTimeInMilliseconds(reminder.timeInMilliseconds)
                                )
                    else resources.getString(R.string.time_not_set)
            }
        }
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }
}