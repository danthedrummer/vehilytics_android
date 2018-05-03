package com.ddowney.vehilytics

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import com.ddowney.vehilytics.services.ServiceManager
import kotlinx.android.synthetic.main.activity_home.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeActivity : AppCompatActivity() {

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
            //TODO: launch device activity
            Log.d(LOG_TAG, "DEVICE button clicked")
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return when (item?.itemId) {
            R.id.sign_out_menu_option -> {
                logout()
                true
            }

            else -> {
                super.onOptionsItemSelected(item)
            }
        }
    }

    private fun logout() {
        if (User.email.isEmpty() || User.token.isEmpty()) {
            User.clearUser()
            val intent = Intent(baseContext, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
        ServiceManager.authenticationService.logout(User.email, User.token)
                .enqueue(object: Callback<Void> {
                    override fun onFailure(call: Call<Void>?, t: Throwable?) {
                        Log.e(LOG_TAG, "Error: ${t?.message}")
                    }

                    override fun onResponse(call: Call<Void>?, response: Response<Void>?) {
                        if (response?.code() == 200) {
                            Log.d(LOG_TAG, "Successfully logged out")
                            User.clearUser()
                            val intent = Intent(baseContext, LoginActivity::class.java)
                            startActivity(intent)
                            finish()
                        } else {
                            Log.e(LOG_TAG, "Problem logging out, code: ${response?.code()}")
                        }
                    }
                })
    }
}
