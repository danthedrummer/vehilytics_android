package com.ddowney.vehilytics.activities

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import com.ddowney.vehilytics.R
import com.ddowney.vehilytics.Vehilytics
import com.ddowney.vehilytics.models.User
import com.ddowney.vehilytics.services.ServiceManager
import kotlinx.android.synthetic.main.activity_splash.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SplashActivity : AppCompatActivity() {

    companion object {
        private const val LOG_TAG = "SplashActivity"
    }

    private lateinit var runnable: Runnable

    private val handler = Handler()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        Vehilytics.retrieveUserFromStorage(baseContext)

        validateUser()

    }

    private fun validateUser() {
        ServiceManager.authenticationService.validate(Vehilytics.user.email, Vehilytics.user.token)
                .enqueue(object: Callback<Void> {
                    override fun onFailure(call: Call<Void>?, t: Throwable?) {
                        Log.e(LOG_TAG, "Error: ${t?.message}")
                        val intent = Intent(baseContext, LoginActivity::class.java)
                        startActivity(intent)
                        finish()
                    }

                    override fun onResponse(call: Call<Void>?, response: Response<Void>?) {
                        val intent = when (response?.code()) {
                            200 -> Intent(baseContext, HomeActivity::class.java)
                            else -> Intent(baseContext, LoginActivity::class.java)
                        }
                        runnable = Runnable {
                            startActivity(intent)
                            finish()
                        }

                        handler.postDelayed(runnable, 3000)

                        splash_image.setOnClickListener {
                            handler.removeCallbacks(runnable)
                            startActivity(intent)
                            finish()
                        }
                    }
                })
    }

    override fun onStop() {
        super.onStop()
        handler.removeCallbacks(runnable)
    }

    override fun onPause() {
        super.onPause()
        handler.removeCallbacks(runnable)
    }
}