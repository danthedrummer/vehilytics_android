package com.ddowney.vehilytics

import android.content.Intent
import android.os.Bundle
import android.util.Log
import kotlinx.android.synthetic.main.activity_home.*

class HomeActivity : DanCompatActivity() {

    companion object {
        private const val LOG_TAG = "HomeActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        setSupportActionBar(main_toolbar)

        vehicle_button.setOnClickListener {
            //TODO: launch vehicle activity
            Log.d(LOG_TAG, "VEHICLE button clicked")
        }

        preferences_button.setOnClickListener {
            //TODO: launch preferences activity
            Log.d(LOG_TAG, "PREFERENCES button clicked")
        }

        reminders_button.setOnClickListener {
            //TODO: launch reminders activity
            Log.d(LOG_TAG, "REMINDERS button clicked")
        }

        device_button.setOnClickListener {
            Log.d(LOG_TAG, "DEVICE button clicked")
            val intent = Intent(baseContext, DeviceActivity::class.java)
            startActivity(intent)
        }
    }

}
