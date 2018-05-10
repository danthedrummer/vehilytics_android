package com.ddowney.vehilytics.activities

import android.graphics.Color
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.widget.TextView
import com.ddowney.vehilytics.R
import com.ddowney.vehilytics.Vehilytics
import com.ddowney.vehilytics.helpers.DanCompatActivity
import com.ddowney.vehilytics.helpers.callbacks.VehilyticsCallback
import com.ddowney.vehilytics.models.Device
import com.ddowney.vehilytics.services.ServiceManager
import kotlinx.android.synthetic.main.activity_device.*
import retrofit2.Call
import retrofit2.Response

/**
 * This is the device manager activity where a user can attach
 * and detach a device from their account.
 */
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
            val deviceName = device_name_field.text.toString()

            if (deviceName.isEmpty()) {
                makeSnackText(getString(R.string.invalid_device_name), device_layout)
                return@setOnClickListener
            }

            if (!Vehilytics.device.deviceName.isEmpty()) {
                makeSnackText(getString(R.string.device_already_attached), device_layout)
                return@setOnClickListener
            }

            attachDevice(deviceName)
            device_name_field.text.clear()
        }

        detach_device_button.setOnClickListener {
            if (Vehilytics.device.deviceName == "") {
                makeSnackText(getString(R.string.no_device_attached), device_layout)
                return@setOnClickListener
            }

            val deviceName = device_name_field.text.toString()
            if (deviceName.isEmpty() || deviceName != Vehilytics.device.deviceName) {
                makeSnackText(getString(R.string.invalid_device_name), device_layout)
                return@setOnClickListener
            }

            detachDevice(deviceName)
            device_name_field.text.clear()
        }
    }

    /**
     * Requests information about the device attached to the current user.
     * Updates the UI to display the name once there is a response.
     */
    private fun getDeviceInfo() {
        ServiceManager.deviceService.getDeviceInfo(Vehilytics.user.email, Vehilytics.user.token)
                .enqueue(object: VehilyticsCallback<Device>(baseContext) {
                    override fun onResponse(call: Call<Device>?, response: Response<Device>?) {
                        super.onResponse(call, response)
                        when (response?.code()) {
                            400 -> {
                                makeSnackText(getString(R.string.no_device_attached), device_layout)
                            }
                            200 -> {
                                val deviceName = response.body()?.deviceName ?: ""
                                val deviceEmail = response.body()?.email ?: ""
                                device_name_text.text = deviceName
                                Vehilytics.device = Device(deviceEmail, deviceName)
                            }
                            else -> {
                                makeSnackText(getString(R.string.invalid_credentials), device_layout)
                            }
                        }
                        attach_device_button.isClickable = true
                        detach_device_button.isClickable = true
                    }
                })
    }

    /**
     * Makes a request to the web service to attach a device to a user.
     *
     * The result of this request is that the device will be attached
     * to the user if it exists or a message will be displayed to describe
     * what went wrong.
     *
     * @param deviceNameToAttach: The name of the device to be attached to the user
     */
    private fun attachDevice(deviceNameToAttach: String) {
        ServiceManager.deviceService.attachDeviceToUser(Vehilytics.user.email, Vehilytics.user.token, deviceNameToAttach)
                .enqueue(object: VehilyticsCallback<Device>(baseContext) {
                    override fun onResponse(call: Call<Device>?, response: Response<Device>?) {
                        super.onResponse(call, response)
                        when (response?.code()) {
                            201 -> {
                                val deviceName = response.body()?.deviceName ?: ""
                                val deviceEmail = response.body()?.email ?: ""
                                Vehilytics.device = Device(deviceEmail, deviceName)
                                device_name_text.text = deviceName
                            }
                            400 -> {
                                makeSnackText(getString(R.string.invalid_device_name), device_layout)
                            }
                            else -> {
                                makeSnackText(getString(R.string.error_occurred), device_layout)
                            }
                        }
                    }
                })
    }

    /**
     * Makes a request to detach the current device from the user
     *
     * @param deviceName: The name of the current user device that they want detached
     */
    private fun detachDevice(deviceName: String) {
        ServiceManager.deviceService.detachDeviceFromUser(Vehilytics.user.email, Vehilytics.user.token, deviceName)
                .enqueue(object: VehilyticsCallback<Void>(baseContext) {
                    override fun onResponse(call: Call<Void>?, response: Response<Void>?) {
                        super.onResponse(call, response)
                        when (response?.code()) {
                            200 -> {
                                device_name_text.text = getString(R.string.none)
                                Vehilytics.clearDevice()
                            }
                            else -> {
                                makeSnackText(getString(R.string.invalid_credentials), device_layout)
                            }
                        }
                    }
                })
    }
}
