package com.ddowney.vehilytics.helpers

import android.annotation.SuppressLint
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import com.ddowney.vehilytics.R
import com.ddowney.vehilytics.Vehilytics
import com.ddowney.vehilytics.activities.LoginActivity
import com.ddowney.vehilytics.services.ServiceManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * This class provides the shared toolbar functionality for multiple activities
 * rather than implementing it multiple times.
 *
 * Suppressing the registered warning because this is not a launchable activity
 */
@SuppressLint("Registered")
open class DanCompatActivity: AppCompatActivity() {

    companion object {
        private const val LOG_TAG = "DanCompatActivity"
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
        if (Vehilytics.user.email.isEmpty() || Vehilytics.user.token.isEmpty()) {
            Vehilytics.clearAll(baseContext)
            val intent = Intent(baseContext, LoginActivity::class.java)
            intent.addFlags (Intent.FLAG_ACTIVITY_NEW_TASK
                    or Intent.FLAG_ACTIVITY_CLEAR_TOP
                    or Intent.FLAG_ACTIVITY_CLEAR_TASK)
            startActivity(intent)
            finish()
        }
        ServiceManager.authenticationService.logout(Vehilytics.user.email, Vehilytics.user.token)
                .enqueue(object: Callback<Void> {
                    override fun onFailure(call: Call<Void>?, t: Throwable?) {
                        Log.e(LOG_TAG, "Error: ${t?.message}")
                    }

                    override fun onResponse(call: Call<Void>?, response: Response<Void>?) {
                        if (response?.code() == 200) {
                            Log.d(LOG_TAG, "Successfully logged out")
                            Vehilytics.clearAll(baseContext)
                            val intent = Intent(baseContext, LoginActivity::class.java)
                            intent.addFlags (Intent.FLAG_ACTIVITY_NEW_TASK
                                    or Intent.FLAG_ACTIVITY_CLEAR_TOP
                                    or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                            startActivity(intent)
                            finish()
                        } else {
                            Log.e(LOG_TAG, "Problem logging out, code: ${response?.code()}")
                        }
                    }
                })
    }
}