package com.ddowney.vehilytics.activities

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.widget.TextView
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
        const val UPDATE_PREFERENCES = 1
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
            startActivityForResult(intent, UPDATE_PREFERENCES)
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == UPDATE_PREFERENCES) {
            if (resultCode == Activity.RESULT_OK) {
                makeSnackText("Preferences Saved")
            }
        }
    }

    /**
     * Creates a snackbar message that pops up from the bottom of
     * the screen
     *
     * @param message: The message to be displayed
     */
    private fun makeSnackText(message: String) {
        val snack = Snackbar.make(home_layout, message, Snackbar.LENGTH_LONG)
        snack.view.findViewById<TextView>(android.support.design.R.id.snackbar_text)
                .setTextColor(Color.WHITE)
        snack.show()
    }

}
