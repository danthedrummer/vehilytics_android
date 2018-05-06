package com.ddowney.vehilytics.activities

import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.View
import com.ddowney.vehilytics.R
import com.ddowney.vehilytics.Vehilytics
import com.ddowney.vehilytics.adapters.VehicleSensorsAdapter
import com.ddowney.vehilytics.helpers.DanCompatActivity
import com.ddowney.vehilytics.helpers.callbacks.VehilyticsCallback
import com.ddowney.vehilytics.helpers.listeners.RecyclerViewClickListener
import com.ddowney.vehilytics.models.ReportedSensorsResponse
import com.ddowney.vehilytics.models.Sensor
import com.ddowney.vehilytics.services.ServiceManager
import kotlinx.android.synthetic.main.activity_vehicle.*
import retrofit2.Call
import retrofit2.Response

class VehicleActivity : DanCompatActivity() {

    companion object {
        private const val LOG_TAG = "VehicleActivity"
    }

    private lateinit var vehicleSensorsAdapter: VehicleSensorsAdapter
    private var vehicleSensors: List<Sensor> = listOf()

    private var warnings: List<String> = listOf()
    private var errors: List<String> = listOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_vehicle)
        setSupportActionBar(vehicle_toolbar)

        vehicle_sensors_recycler.setHasFixedSize(true)
        vehicle_sensors_recycler.layoutManager = LinearLayoutManager(this)

        getReportedSensors()

    }

    fun displayMainContent() {
        vehicle_content_loading.visibility = View.GONE
        vehicle_sensors_recycler.visibility = View.VISIBLE
        updateAdapter()
    }

    private fun updateAdapter() {
        vehicleSensorsAdapter = VehicleSensorsAdapter(vehicleSensors, warnings, errors,
                object: RecyclerViewClickListener {
            override fun onItemClicked(position: Int) {
                Log.d(LOG_TAG, "Clicked ${vehicleSensors[position]}")
                val intent = Intent(baseContext, GraphReadingActivity::class.java)
                intent.putExtra("sensor", vehicleSensors[position])
                startActivity(intent)
            }
        })
        vehicle_sensors_recycler.adapter = vehicleSensorsAdapter
        vehicle_sensors_recycler.hasPendingAdapterUpdates()
    }

    private fun getReportedSensors() {
        ServiceManager.sensorsService.getReportedSensors(Vehilytics.user.email, Vehilytics.user.token)
                .enqueue(object: VehilyticsCallback<ReportedSensorsResponse>() {
                    override fun onResponse(call: Call<ReportedSensorsResponse>?,
                                            response: Response<ReportedSensorsResponse>?) {
                        vehicleSensors = response?.body()?.sensors ?: listOf()
                        warnings = response?.body()?.warnings ?: listOf()
                        errors = response?.body()?.errors ?: listOf()
                        displayMainContent()
                    }
                })
    }
}
