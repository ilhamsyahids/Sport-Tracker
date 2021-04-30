package com.myandroid.sporttracker.adapters

import android.util.Log
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import com.myandroid.sporttracker.R
import com.myandroid.sporttracker.db.Sport
import kotlinx.android.synthetic.main.fragment_track_per_day.view.*
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

class TrackAdapter : RecyclerView.Adapter<TrackAdapter.TrackViewHolder>() {

    inner class TrackViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    val diffCallback = object : DiffUtil.ItemCallback<Sport>(){
        override fun areItemsTheSame(oldItem: Sport, newItem: Sport): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Sport, newItem: Sport): Boolean {
            return oldItem.hashCode() == newItem.hashCode()
        }
    }

    val differ = AsyncListDiffer(this, diffCallback)

    fun submitList(list: List<Sport>) = differ.submitList(list)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackViewHolder {
        return TrackViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.fragment_track_per_day, parent, false))
    }

    override fun onBindViewHolder(holder: TrackViewHolder, position: Int) {
        val track = differ.currentList[position]
        Log.println(Log.ERROR, "ADAPTER", "track")
        holder.itemView.apply{
            category.text = track.type.toString()
            val sdf = SimpleDateFormat("dd/MM/yy hh:mm:ss")
            val netDate = Date(track.timestamp)
            val date =sdf.format(netDate)
            timestamp.text = date

            distance.text = track.distanceInMeters.toString() + " meters"

            duration.text = String.format("%02d min, %02d sec",
                    TimeUnit.MILLISECONDS.toMinutes(track.timeInMillis),
                    TimeUnit.MILLISECONDS.toSeconds(track.timeInMillis) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(track.timeInMillis)))
        }
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }
}