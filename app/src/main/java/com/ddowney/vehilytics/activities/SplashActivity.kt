package com.ddowney.vehilytics.activities

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.ddowney.vehilytics.R
import kotlinx.android.synthetic.main.activity_splash.*

class SplashActivity : AppCompatActivity() {

    private val launchIntent: Intent by lazy { Intent(baseContext, LoginActivity::class.java) }

    private val runnable = Runnable {
        startActivity(launchIntent)
        finish()
    }

    private val handler = Handler()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        handler.postDelayed(runnable, 5000)

        splash_image.setOnClickListener {
            handler.removeCallbacks(runnable)
            startActivity(launchIntent)
            finish()
        }

        // TODO: Load user data from storage
        // If there is anything stored then validate the login token and route
        // to either the login or home screen as appropriate.
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
