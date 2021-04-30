package com.myandroid.sporttracker.ui.track

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.LiveData
import com.myandroid.sporttracker.R
import com.myandroid.sporttracker.db.Sport
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

class TrackPerDayAdapter(
    private val values: LiveData<List<Sport>>

) : RecyclerView.Adapter<TrackPerDayAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.fragment_track_per_day, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val sportss = values.value
        val item = sportss?.get(position)

        if (item != null) {
            holder.category.text = item.type.toString()
            val sdf = SimpleDateFormat("dd/MM/yy hh:mm:ss")
            val netDate = Date(item.timestamp)
            val date =sdf.format(netDate)
            holder.timestamp.text = date

            holder.distance.text = item.distanceInMeters.toString() + " m"

            holder.duration.text = String.format("%02d min, %02d sec",
                TimeUnit.MILLISECONDS.toMinutes(item.timeInMillis),
                TimeUnit.MILLISECONDS.toSeconds(item.timeInMillis) -
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(item.timeInMillis))
            );
        }

    }

    override fun getItemCount(): Int = values.value?.size!!

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val category: TextView = view.findViewById(R.id.category)
        val timestamp: TextView = view.findViewById(R.id.timestamp)
        val distance: TextView = view.findViewById(R.id.distance)
        val duration: TextView = view.findViewById(R.id.duration)
    }
}