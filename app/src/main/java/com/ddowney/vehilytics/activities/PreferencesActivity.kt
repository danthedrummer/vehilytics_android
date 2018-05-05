package com.ddowney.vehilytics.activities

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.View
import com.ddowney.vehilytics.R
import com.ddowney.vehilytics.Vehilytics
import com.ddowney.vehilytics.adapters.SensorPreferencesAdapter
import com.ddowney.vehilytics.helpers.DanCompatActivity
import com.ddowney.vehilytics.helpers.callbacks.VehilyticsCallback
import com.ddowney.vehilytics.helpers.listeners.SensorListClickListener
import com.ddowney.vehilytics.models.Sensor
import com.ddowney.vehilytics.models.UpdateSensorsRequest
import com.ddowney.vehilytics.services.ServiceManager
import kotlinx.android.synthetic.main.activity_preferences.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PreferencesActivity : DanCompatActivity() {

    companion object {
        private const val LOG_TAG = "PreferencesActivity"
    }

    private lateinit var sensorPreferencesAdapter: SensorPreferencesAdapter
    private var sensorList: List<Sensor> = listOf()

    private var sensorListRetrieved = false
    private var sensorPreferencesRetrieved = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_preferences)
        setSupportActionBar(preferences_toolbar)

        sensor_list_recycler.setHasFixedSize(true)
        sensor_list_recycler.layoutManager = LinearLayoutManager(this)

        getSupportedSensors()
        getSensorPreferences()

    }

    private fun displayMainContent() {
        updateAdapter()

        preferences_fab.setOnClickListener {
            updateSensorPreferences()
        }

        preferences_content_loading.visibility = View.GONE
        sensor_list_recycler.visibility = View.VISIBLE
        preferences_fab.visibility = View.VISIBLE
    }

    private fun updateAdapter() {
        sensorPreferencesAdapter = SensorPreferencesAdapter(sensorList, object: SensorListClickListener {
            override fun onItemClicked(position: Int) {
                val sensor = sensorList[position]

                val preferences = Vehilytics.sensorPreferences.toMutableMap()

                if (!Vehilytics.sensorPreferences.containsKey(sensor.shortname)) {
                    preferences[sensor.shortname] = sensor
                } else {
                    preferences.remove(sensor.shortname)
                }

                Vehilytics.sensorPreferences = preferences

                Log.d(LOG_TAG, Vehilytics.sensorPreferences.toString())
            }
        })
        sensor_list_recycler.adapter = sensorPreferencesAdapter
        sensor_list_recycler.hasPendingAdapterUpdates()
    }

    private fun getSupportedSensors() {
        ServiceManager.sensorsService.getAllSensors()
                .enqueue(object: VehilyticsCallback<List<Sensor>>() {
                    override fun onResponse(call: Call<List<Sensor>>?, response: Response<List<Sensor>>?) {
                        when (response?.code()) {
                            200 -> {
                                Log.d(LOG_TAG, "Body: ${response.body()}")
                                sensorList = response.body() ?: listOf()
                                sensorListRetrieved = true
                                if (sensorListRetrieved && sensorPreferencesRetrieved) {
                                    displayMainContent()
                                }
                            }
                            else -> {
                                Log.e(LOG_TAG, "Response: ${response?.code()}")
                            }
                        }
                    }
                })
    }

    private fun getSensorPreferences() {
        ServiceManager.sensorsService.getRequestedSensors(Vehilytics.user.email,
                Vehilytics.user.token, "requestedSensors")
                .enqueue(object: VehilyticsCallback<List<Sensor>>() {

                    override fun onResponse(call: Call<List<Sensor>>?, response: Response<List<Sensor>>?) {
                        when (response?.code()) {
                            200 -> {
                                Log.d(LOG_TAG, "Body: ${response.body()}")
                                val preferences = response.body() ?: listOf()
                                val preferencesMap = mutableMapOf<String, Sensor>()
                                preferences.forEach {
                                    preferencesMap[it.shortname] = it
                                }
                                Vehilytics.sensorPreferences = preferencesMap
                                sensorPreferencesRetrieved = true
                                if (sensorListRetrieved && sensorPreferencesRetrieved) {
                                    displayMainContent()
                                }
                            }
                            else -> {
                                Log.e(LOG_TAG, "Response: ${response?.code()}")
                            }
                        }
                    }
                })
    }

    private fun updateSensorPreferences() {
        val preferences = mutableListOf<String>()
        Vehilytics.sensorPreferences.forEach { (_, sensor) ->
            preferences.add(sensor.shortname)
        }

        ServiceManager.sensorsService.updateRequestedSensors(Vehilytics.user.email,
                Vehilytics.user.token, UpdateSensorsRequest(preferences))
                .enqueue(object: VehilyticsCallback<Void>() {
                    override fun onResponse(call: Call<Void>?, response: Response<Void>?) {
                        when (response?.code()) {
                            201 -> finish()
                            else -> {
                                Log.d(LOG_TAG, "There was an issue with the request: ${response?.code()}")
                            }
                        }
                    }
                })
    }
}
