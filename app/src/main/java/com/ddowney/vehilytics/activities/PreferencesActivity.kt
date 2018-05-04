package com.ddowney.vehilytics.activities

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import com.ddowney.vehilytics.R
import com.ddowney.vehilytics.adapters.SensorPreferencesAdapter
import com.ddowney.vehilytics.helpers.DanCompatActivity
import com.ddowney.vehilytics.helpers.SensorListClickListener
import com.ddowney.vehilytics.models.Sensor
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
    private var sensorList: List<Sensor> = listOf(Sensor("1", "test1", "test1", "?"))

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_preferences)
        setSupportActionBar(preferences_toolbar)

        sensor_list_recycler.setHasFixedSize(true)
        sensor_list_recycler.layoutManager = LinearLayoutManager(this)

        getSupportedSensors()
//        updateAdapter()
    }

    private fun updateAdapter() {
        sensorPreferencesAdapter = SensorPreferencesAdapter(sensorList, object: SensorListClickListener {
            override fun onItemClicked(position: Int) {

            }
        })
        sensor_list_recycler.adapter = sensorPreferencesAdapter
        sensor_list_recycler.hasPendingAdapterUpdates()
    }

    private fun getSupportedSensors() {
        ServiceManager.sensorsService.getAllSensors()
                .enqueue(object: Callback<List<Sensor>> {
                    override fun onFailure(call: Call<List<Sensor>>?, t: Throwable?) {
                        Log.e(LOG_TAG, "Error: ${t?.message}")
                    }

                    override fun onResponse(call: Call<List<Sensor>>?, response: Response<List<Sensor>>?) {
                        when (response?.code()) {
                            200 -> {
                                Log.e(LOG_TAG, "Body: ${response.body()}")
                                sensorList = response.body() ?: listOf()
                                updateAdapter()
                            }
                            else -> {
                                Log.e(LOG_TAG, "Response: ${response?.code()}")
                            }
                        }
                    }
                })
    }
}
