package com.ddowney.vehilytics.adapters

import android.content.res.Resources
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.ddowney.vehilytics.R
import com.ddowney.vehilytics.Vehilytics
import com.ddowney.vehilytics.helpers.listeners.RecyclerViewClickListener
import com.ddowney.vehilytics.models.Sensor
import kotlinx.android.synthetic.main.sensor_preference_view.view.*
import java.lang.ref.WeakReference

/**
 * Adapter for the preferences screen recycler view
 */
class SensorPreferencesAdapter(private val data: List<Sensor>,
                               private val listener: RecyclerViewClickListener)
    : RecyclerView.Adapter<SensorPreferencesAdapter.ViewHolder>() {

    class ViewHolder(itemView: View, listener: RecyclerViewClickListener)
        : RecyclerView.ViewHolder(itemView), View.OnClickListener {

        private val listenerRef: WeakReference<RecyclerViewClickListener> = WeakReference(listener)

        fun bindSensorModel(sensor: Sensor) {
            itemView.sensor_name.text = sensor.name
            itemView.sensor_shortname.text = sensor.shortname
            itemView.preferences_item.setOnClickListener(this@ViewHolder)
        }

        override fun onClick(v: View) {
            itemView.sensor_checkbox.isChecked = !itemView.sensor_checkbox.isChecked
            listenerRef.get()?.onItemClicked(adapterPosition)
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater
                .from(parent.context)
                .inflate(R.layout.sensor_preference_view, parent, false)
        return ViewHolder(v, listener)
    }

    override fun getItemCount() = data.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindSensorModel(data[position])
        if (Vehilytics.sensorPreferences.containsKey(data[position].shortname)) {
            holder.itemView.sensor_checkbox.isChecked = true
        }
        if (position + 1 == itemCount) {
            setBottomMargin(holder.itemView, (64 * Resources.getSystem().displayMetrics.density).toInt())
        }
    }

    /**
     * Increases the bottom margin of the last item in the list so that there is
     * enough space at the end for the floating action button to sit freely
     *
     * @param view: The view to be updated
     * @param bottomMargin: The size of new bottom margin in dp
     */
    private fun setBottomMargin(view: View, bottomMargin: Int) {
        if (view.layoutParams is ViewGroup.MarginLayoutParams) {
            val params = view.layoutParams as ViewGroup.MarginLayoutParams
            params.setMargins(params.leftMargin, params.topMargin, params.rightMargin, bottomMargin)
            view.requestLayout()
        }
    }

}