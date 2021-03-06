package com.ddowney.vehilytics.adapters

import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.ddowney.vehilytics.R
import com.ddowney.vehilytics.helpers.listeners.RecyclerViewClickListener
import com.ddowney.vehilytics.models.Sensor
import kotlinx.android.synthetic.main.vehicle_sensor_view.view.*
import java.lang.ref.WeakReference

/**
 * Adapter for the vehicle activity recycler view
 */
class VehicleSensorsAdapter(private val data: List<Sensor>, private val warnings: List<String>,
                            private val errors: List<String>,
                            private val listener: RecyclerViewClickListener)
    : RecyclerView.Adapter<VehicleSensorsAdapter.ViewHolder>() {

    class ViewHolder(itemView: View, listener: RecyclerViewClickListener)
        : RecyclerView.ViewHolder(itemView), View.OnClickListener {

        companion object {
            private const val LOG_TAG = "VehicleSensorsHolder"
        }

        private val listenerRef: WeakReference<RecyclerViewClickListener> = WeakReference(listener)

        fun bindSensorModel(sensor: Sensor) {
            itemView.sensor_name.text = sensor.name
            itemView.sensor_shortname.text = sensor.shortname

            itemView.vehicle_sensor_item.setOnClickListener(this)
        }

        override fun onClick(v: View) {
            Log.d(LOG_TAG, "Vehicle Sensor Clicked: $adapterPosition")
            listenerRef.get()?.onItemClicked(adapterPosition)
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater
                .from(parent.context)
                .inflate(R.layout.vehicle_sensor_view, parent, false)
        return ViewHolder(v, listener)
    }

    override fun getItemCount() = data.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindSensorModel(data[position])
        if (data[position].shortname in errors) {
            holder.itemView.vehicle_sensor_error.visibility = View.VISIBLE
        } else if (data[position].shortname in warnings) {
            holder.itemView.vehicle_sensor_warning.visibility = View.VISIBLE
        }
    }

}