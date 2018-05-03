package com.ddowney.vehilytics

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.ddowney.vehilytics.models.LoginResponse
import com.ddowney.vehilytics.models.RegistrationRequest
import com.ddowney.vehilytics.models.RegistrationResponse
import com.ddowney.vehilytics.models.UserRegistration
import com.ddowney.vehilytics.services.ServiceManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity() {

    companion object {
        private const val LOG_TAG = "LoginActivity"
    }

    var token: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        login()
//        register()
    }

    private fun login() {
        ServiceManager.authenticationService.login("dan@example.com", "password")
                .enqueue(object: Callback<LoginResponse> {
                    override fun onFailure(call: Call<LoginResponse>?, t: Throwable?) {
                        Log.e(LOG_TAG, "Error: ${t?.message}")
                    }

                    override fun onResponse(call: Call<LoginResponse>?, response: Response<LoginResponse>?) {
                        Log.d(LOG_TAG, "dan user token = ${response?.body()?.token}")
                        token = response?.body()?.token!!
                        validate()
                    }

                })
    }

    private fun logout() {
        ServiceManager.authenticationService.logout("dan@example.com", token)
                .enqueue(object: Callback<Void>{
                    override fun onFailure(call: Call<Void>?, t: Throwable?) {
                        Log.e(LOG_TAG, "Error: ${t?.message}")
                    }

                    override fun onResponse(call: Call<Void>?, response: Response<Void>?) {
                        if (response?.code() == 200) {
                            Log.d(LOG_TAG, "Successfully logged out")
                        } else {
                            Log.e(LOG_TAG, "Problem logging out, code: ${response?.code()}")
                        }
                    }

                })
    }

    private fun register() {
        ServiceManager.authenticationService.register(RegistrationRequest(UserRegistration("test2@example.com", "password", "password")))
                .enqueue(object: Callback<RegistrationResponse> {
                    override fun onFailure(call: Call<RegistrationResponse>?, t: Throwable?) {
                        Log.e(LOG_TAG, "Error: ${t?.message}")
                    }

                    override fun onResponse(call: Call<RegistrationResponse>?, response: Response<RegistrationResponse>?) {
                        Log.d(LOG_TAG, "new user token = ${response?.body()?.token}")
                    }

                })
    }

    private fun validate() {
        ServiceManager.authenticationService.validate("dan@example.com", token)
                .enqueue(object: Callback<Void> {
                    override fun onFailure(call: Call<Void>?, t: Throwable?) {
                        Log.e(LOG_TAG, "Error: ${t?.message}")
                    }

                    override fun onResponse(call: Call<Void>?, response: Response<Void>?) {
                        Log.d(LOG_TAG, "Validate code: ${response?.code()}")
                    }
                })
    }
}
