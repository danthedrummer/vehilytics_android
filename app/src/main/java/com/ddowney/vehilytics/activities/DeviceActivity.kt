package com.ddowney.vehilytics.activities

import android.os.Bundle
import android.util.Log
import android.view.View
import com.ddowney.vehilytics.R
import com.ddowney.vehilytics.User
import com.ddowney.vehilytics.helpers.DanCompatActivity
import com.ddowney.vehilytics.models.Device
import com.ddowney.vehilytics.services.ServiceManager
import kotlinx.android.synthetic.main.activity_device.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DeviceActivity : DanCompatActivity() {

    companion object {
        private const val LOG_TAG = "DeviceActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_device)
        setSupportActionBar(device_manager_toolbar)

        getDeviceInfo()

        attach_device_button.setOnClickListener {
            device_manager_error_text.visibility = View.GONE
            val deviceName = device_name_field.text.toString()

            if (deviceName.isEmpty()) {
                device_manager_error_text.text = getString(R.string.invalid_device_name)
                device_manager_error_text.visibility = View.VISIBLE
                return@setOnClickListener
            }

            if (!User.deviceName.isEmpty()) {
                device_manager_error_text.text = getString(R.string.device_already_attached)
                device_manager_error_text.visibility = View.VISIBLE
                return@setOnClickListener
            }

            attachDevice(deviceName)
            device_name_field.text.clear()
        }

        detach_device_button.setOnClickListener {
            device_manager_error_text.visibility = View.GONE

            if (User.deviceName == "") {
                //TODO: alert the user that no device is attached
                return@setOnClickListener
            }

            val deviceName = device_name_field.text.toString()
            if (deviceName.isEmpty() || deviceName != User.deviceName) {
                device_manager_error_text.text = getString(R.string.invalid_device_name)
                device_manager_error_text.visibility = View.VISIBLE
                return@setOnClickListener
            }

            detachDevice(deviceName)
            device_name_field.text.clear()
        }
    }

    private fun getDeviceInfo() {
        ServiceManager.deviceService.getDeviceInfo(User.email, User.token)
                .enqueue(object: Callback<Device> {
                    override fun onFailure(call: Call<Device>?, t: Throwable?) {
                        Log.e(LOG_TAG, "Error: ${t?.message}")
                    }

                    override fun onResponse(call: Call<Device>, response: Response<Device>) {
                        Log.d(LOG_TAG, "code = ${response.code()}")
                        when (response.code()) {
                            400 -> {
                                device_manager_error_text.text = getString(R.string.no_device_attached)
                                device_manager_error_text.visibility = View.VISIBLE
                            }
                            200 -> {
                                val deviceName = response.body()?.deviceName
                                if (!deviceName.isNullOrEmpty()) {
                                    device_name_text.text = deviceName
                                    User.deviceName = deviceName ?: "None"
                                }
                            }
                            else -> {
                                device_manager_error_text.text = getString(R.string.invalid_credentials)
                                device_manager_error_text.visibility = View.VISIBLE
                            }
                        }
                        attach_device_button.isClickable = true
                        detach_device_button.isClickable = true
                    }
                })
    }

    private fun attachDevice(deviceName: String) {
        ServiceManager.deviceService.attachDeviceToUser(User.email, User.token, deviceName)
                .enqueue(object: Callback<Device> {
                    override fun onFailure(call: Call<Device>?, t: Throwable?) {
                        Log.e(LOG_TAG, "Error: ${t?.message}")
                    }

                    override fun onResponse(call: Call<Device>?, response: Response<Device>?) {
                        when (response?.code()) {
                            201 -> {
                                User.deviceName = response.body()?.deviceName ?: "None"
                                device_name_text.text = User.deviceName
                            }
                            400 -> {
                                device_manager_error_text.text = getString(R.string.invalid_device_name)
                                device_manager_error_text.visibility = View.VISIBLE
                            }
                            else -> {
                                device_manager_error_text.text = getString(R.string.error_occurred)
                                device_manager_error_text.visibility = View.VISIBLE
                            }
                        }
                    }
                })
    }

    private fun detachDevice(deviceName: String) {
        ServiceManager.deviceService.detachDeviceFromUser(User.email, User.token, deviceName)
                .enqueue(object: Callback<Void> {
                    override fun onFailure(call: Call<Void>?, t: Throwable?) {
                        Log.e(LOG_TAG, "Error: ${t?.message}")
                    }

                    override fun onResponse(call: Call<Void>?, response: Response<Void>?) {
                        when (response?.code()) {
                            200 -> {
                                device_name_text.text = getString(R.string.none)
                                User.deviceName = ""
                            }
                            else -> {
                                device_manager_error_text.text = getString(R.string.invalid_credentials)
                                device_manager_error_text.visibility = View.VISIBLE
                            }
                        }
                    }
                })
    }
}
