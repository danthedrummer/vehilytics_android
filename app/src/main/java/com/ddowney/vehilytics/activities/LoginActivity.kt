package com.ddowney.vehilytics.activities

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import com.ddowney.vehilytics.R
import com.ddowney.vehilytics.Vehilytics
import com.ddowney.vehilytics.helpers.callbacks.VehilyticsCallback
import com.ddowney.vehilytics.models.LoginResponse
import com.ddowney.vehilytics.models.User
import com.ddowney.vehilytics.services.ServiceManager
import kotlinx.android.synthetic.main.activity_login.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity() {

    companion object {
        private const val LOG_TAG = "LoginActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        register_prompt.setOnClickListener {
            val intent = Intent(baseContext, RegisterActivity::class.java)
            startActivity(intent)
            finish()
        }

        login_button.setOnClickListener {
            error_text.visibility = View.GONE
            it.isClickable = false
            val email = email_field.text.toString()
            val password = password_field.text.toString()

            if (email.isEmpty() || password.isEmpty()) {
                error_text.text = getString(R.string.blank_fields_error)
                error_text.visibility = View.VISIBLE
                it.isClickable = true
                return@setOnClickListener
            }

            login(email, password)
        }

    }

    private fun login(email: String, password: String) {
        ServiceManager.authenticationService.login(email, password)
                .enqueue(object: VehilyticsCallback<LoginResponse>() {
                    override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                        if (response.code() == 201) {
                            Vehilytics.user = User(response.body()?.email ?: "",
                                    response.body()?.token ?: "")
                            Vehilytics.storeUser(baseContext)
                            val intent = Intent(baseContext, HomeActivity::class.java)
                            startActivity(intent)
                            finish()
                        } else if (response.code() == 401) {
                            Log.d(LOG_TAG, "Invalid Credentials")
                            error_text.text = getString(R.string.email_or_password_invalid)
                            error_text.visibility = View.VISIBLE
                            login_button.isClickable = true
                        }
                    }
                })
    }

    private fun validate() {
        ServiceManager.authenticationService.validate("dan@example.com", "some_token")
                .enqueue(object: VehilyticsCallback<Void>() {
                    override fun onResponse(call: Call<Void>?, response: Response<Void>?) {
                        Log.d(LOG_TAG, "Validate code: ${response?.code()}")
                    }
                })
    }
}
