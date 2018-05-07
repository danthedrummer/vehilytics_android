package com.ddowney.vehilytics.activities

import android.content.Intent
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_home.*
import com.ddowney.vehilytics.R
import com.ddowney.vehilytics.helpers.DanCompatActivity

/**
 * The home activity is the main menu of the application and is the
 * primary navigation point for reaching each of the features
 */
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
            val intent = Intent(baseContext, RemindersActivity::class.java)
            startActivity(intent)
        }

        device_button.setOnClickListener {
            val intent = Intent(baseContext, DeviceActivity::class.java)
            startActivity(intent)
        }

    }

}
