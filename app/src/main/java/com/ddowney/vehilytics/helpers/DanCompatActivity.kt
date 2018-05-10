package com.ddowney.vehilytics.helpers

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import com.ddowney.vehilytics.R
import com.ddowney.vehilytics.Vehilytics
import com.ddowney.vehilytics.activities.LoginActivity
import com.ddowney.vehilytics.helpers.callbacks.VehilyticsCallback
import com.ddowney.vehilytics.services.ServiceManager
import com.ddowney.vehilytics.storage.Storage
import retrofit2.Call
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
                if (!Vehilytics.user.email.isEmpty() || !Vehilytics.user.token.isEmpty()) {
                    logoutRequest()
                } else {
                    logoutLocal()
                }
                true
            }

            else -> {
                super.onOptionsItemSelected(item)
            }
        }
    }

    /**
     * Makes a request to sign out the current user, resetting their authentication token
     */
    private fun logoutRequest() {
        ServiceManager.authenticationService.logout(Vehilytics.user.email, Vehilytics.user.token)
                .enqueue(object : VehilyticsCallback<Void>(baseContext) {
                    override fun onFailure(call: Call<Void>?, t: Throwable?) {
                        super.onFailure(call, t)
                        logoutLocal()
                    }

                    override fun onResponse(call: Call<Void>?, response: Response<Void>?) {
                        super.onResponse(call, response)
                        if (response?.code() == 200) {
                            Log.d(LOG_TAG, "Successfully logged out")
                        } else {
                            Log.e(LOG_TAG, "Problem logging out, code: ${response?.code()}")
                        }
                        logoutLocal()
                    }
                })
    }

    /**
     * Removes all user credentials from the device, forcing a login next time
     * the application is started
     */
    private fun logoutLocal() {
        Vehilytics.clearAll(Storage(baseContext))
        val intent = Intent(baseContext, LoginActivity::class.java)
        intent.addFlags (Intent.FLAG_ACTIVITY_NEW_TASK
                or Intent.FLAG_ACTIVITY_CLEAR_TOP
                or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        startActivity(intent)
        finish()
    }

    /**
     * Creates a snackbar message that pops up from the bottom of
     * the screen
     *
     * @param message: The message to be displayed
     * @param view: The view to display the snackbar
     */
    protected fun makeSnackText(message: String, view: View) {
        val snack = Snackbar.make(view, message, Snackbar.LENGTH_LONG)
        snack.view.findViewById<TextView>(android.support.design.R.id.snackbar_text)
                .setTextColor(Color.WHITE)
        snack.show()
    }
}