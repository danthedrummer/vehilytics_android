package com.ddowney.vehilytics.activities

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import com.ddowney.vehilytics.R
import com.ddowney.vehilytics.Vehilytics
import com.ddowney.vehilytics.models.RegistrationRequest
import com.ddowney.vehilytics.models.RegistrationResponse
import com.ddowney.vehilytics.models.User
import com.ddowney.vehilytics.models.UserRegistration
import com.ddowney.vehilytics.services.ServiceManager
import kotlinx.android.synthetic.main.activity_register.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterActivity : AppCompatActivity() {

    companion object {
        private const val LOG_TAG = "RegisterActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        login_prompt.setOnClickListener {
            val intent = Intent(baseContext, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }

        register_button.setOnClickListener {
            error_text.visibility = View.GONE
            it.isClickable = false
            val email = email_field.text.toString()
            val password = password_field.text.toString()
            val passwordConfirm = password_confirm_field.text.toString()

            if (email.isEmpty() || password.isEmpty() || passwordConfirm.isEmpty()) {
                error_text.text = getString(R.string.blank_fields_error)
                error_text.visibility = View.VISIBLE
                it.isClickable = true
                return@setOnClickListener
            }

            if (password != passwordConfirm) {
                error_text.text = getString(R.string.password_mismatch_error)
                error_text.visibility = View.VISIBLE
                it.isClickable = true
                return@setOnClickListener
            }

            register(email, password, passwordConfirm)
        }
    }

    private fun register(email: String, password: String, passwordConfirm: String) {
        ServiceManager.authenticationService
                .register(RegistrationRequest(UserRegistration(email, password, passwordConfirm)))
                .enqueue(object: Callback<RegistrationResponse> {
                    override fun onFailure(call: Call<RegistrationResponse>, t: Throwable) {
                        Log.e(LOG_TAG, "Error while registering: ${t.message}")
                    }

                    override fun onResponse(call: Call<RegistrationResponse>, response: Response<RegistrationResponse>) {
                        if (response.code() == 201) {
                            // TODO: Store token
                            Vehilytics.user = User(email , response.body()?.token ?: "")
                            val intent = Intent(baseContext, HomeActivity::class.java)
                            startActivity(intent)
                            finish()
                        } else if (response.code() == 401) {
                            Log.d(LOG_TAG, "Problem registering. Code: ${response.code()}")
                            error_text.text = getString(R.string.email_in_use_error)
                            error_text.visibility = View.VISIBLE
                            register_button.isClickable = true
                        }
                    }
                })
    }
}
