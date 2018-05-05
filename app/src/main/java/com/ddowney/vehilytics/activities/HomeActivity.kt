package com.ddowney.vehilytics.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import kotlinx.android.synthetic.main.activity_home.*
import com.ddowney.vehilytics.R
import com.ddowney.vehilytics.Vehilytics
import com.ddowney.vehilytics.helpers.DanCompatActivity


class HomeActivity : DanCompatActivity() {

    companion object {
        private const val LOG_TAG = "HomeActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        setSupportActionBar(home_toolbar)

        vehicle_button.setOnClickListener {
            val intent = Intent(baseContext, VehicleActivity::class.java)
            startActivity(intent)
        }

        preferences_button.setOnClickListener {
            val intent = Intent(baseContext, PreferencesActivity::class.java)
            startActivity(intent)
        }

        reminders_button.setOnClickListener {
            //TODO: launch reminders activity
            Log.d(LOG_TAG, "REMINDERS button clicked")
        }

        device_button.setOnClickListener {
            val intent = Intent(baseContext, DeviceActivity::class.java)
            startActivity(intent)
        }

    }

}
