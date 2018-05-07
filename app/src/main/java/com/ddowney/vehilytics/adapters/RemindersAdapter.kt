package com.ddowney.vehilytics.adapters

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.ddowney.vehilytics.R
import com.ddowney.vehilytics.helpers.listeners.RecyclerViewClickListener
import com.ddowney.vehilytics.models.Reminder
import kotlinx.android.synthetic.main.reminder_item_view.view.*
import java.lang.ref.WeakReference

class RemindersAdapter(private val data: List<Reminder>,
                               private val listener: RecyclerViewClickListener)
    : RecyclerView.Adapter<RemindersAdapter.ViewHolder>() {

    class ViewHolder(itemView: View, listener: RecyclerViewClickListener)
        : RecyclerView.ViewHolder(itemView), View.OnClickListener {

        private val listenerRef: WeakReference<RecyclerViewClickListener> = WeakReference(listener)

        fun bindSensorModel(reminder: Reminder) {
            val title = reminder.title
            var frequency = "Every "
            frequency += when {
                reminder.weeklyFrequency == 1 -> "${reminder.weeklyFrequency} week"
                reminder.weeklyFrequency in 2..4 -> "${reminder.weeklyFrequency} weeks"
                reminder.weeklyFrequency in 4..7 -> "${reminder.weeklyFrequency/4} month"
                reminder.weeklyFrequency in 8..51 -> "${reminder.weeklyFrequency/4} months"
                reminder.weeklyFrequency in 52..103 -> "${reminder.weeklyFrequency/52} year"
                else -> "${reminder.weeklyFrequency/52} years"
            }
            itemView.reminder_name.text = title
            itemView.reminder_frequency.text = frequency
            itemView.reminder_item.setOnClickListener(this)
        }

        override fun onClick(v: View) {
            listenerRef.get()?.onItemClicked(adapterPosition)
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater
                .from(parent.context)
                .inflate(R.layout.reminder_item_view, parent, false)
        return ViewHolder(v, listener)
    }

    override fun getItemCount() = data.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindSensorModel(data[position])
    }

}