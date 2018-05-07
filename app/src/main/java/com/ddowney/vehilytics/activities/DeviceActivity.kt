package com.ddowney.vehilytics.activities

import android.graphics.Color
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.util.Log
import android.widget.TextView
import com.ddowney.vehilytics.R
import com.ddowney.vehilytics.Vehilytics
import com.ddowney.vehilytics.helpers.DanCompatActivity
import com.ddowney.vehilytics.helpers.callbacks.VehilyticsCallback
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
            val deviceName = device_name_field.text.toString()

            if (deviceName.isEmpty()) {
                makeSnackText(getString(R.string.invalid_device_name))
                return@setOnClickListener
            }

            if (!Vehilytics.device.deviceName.isEmpty()) {
                makeSnackText(getString(R.string.device_already_attached))
                return@setOnClickListener
            }

            attachDevice(deviceName)
            device_name_field.text.clear()
        }

        detach_device_button.setOnClickListener {
            if (Vehilytics.device.deviceName == "") {
                makeSnackText(getString(R.string.no_device_attached))
                return@setOnClickListener
            }

            val deviceName = device_name_field.text.toString()
            if (deviceName.isEmpty() || deviceName != Vehilytics.device.deviceName) {
                makeSnackText(getString(R.string.invalid_device_name))
                return@setOnClickListener
            }

            detachDevice(deviceName)
            device_name_field.text.clear()
        }
    }

    private fun makeSnackText(message: String) {
        val snack = Snackbar.make(device_layout, message, Snackbar.LENGTH_LONG)
        snack.view.findViewById<TextView>(android.support.design.R.id.snackbar_text)
                .setTextColor(Color.WHITE)
        snack.show()
    }

    private fun getDeviceInfo() {
        ServiceManager.deviceService.getDeviceInfo(Vehilytics.user.email, Vehilytics.user.token)
                .enqueue(object: VehilyticsCallback<Device>(baseContext) {
                    override fun onResponse(call: Call<Device>?, response: Response<Device>?) {
                        super.onResponse(call, response)
                        when (response?.code()) {
                            400 -> {
                                makeSnackText(getString(R.string.no_device_attached))
                            }
                            200 -> {
                                val deviceName = response.body()?.deviceName ?: ""
                                val deviceEmail = response.body()?.email ?: ""
                                device_name_text.text = deviceName
                                Vehilytics.device = Device(deviceEmail, deviceName)
                            }
                            else -> {
                                makeSnackText(getString(R.string.invalid_credentials))
                            }
                        }
                        attach_device_button.isClickable = true
                        detach_device_button.isClickable = true
                    }
                })
    }

    private fun attachDevice(deviceNameToAttach: String) {
        ServiceManager.deviceService.attachDeviceToUser(Vehilytics.user.email, Vehilytics.user.token, deviceNameToAttach)
                .enqueue(object: VehilyticsCallback<Device>(baseContext) {
                    override fun onResponse(call: Call<Device>?, response: Response<Device>?) {
                        when (response?.code()) {
                            201 -> {
                                val deviceName = response.body()?.deviceName ?: ""
                                val deviceEmail = response.body()?.email ?: ""
                                Vehilytics.device = Device(deviceEmail, deviceName)
                                device_name_text.text = deviceName
                            }
                            400 -> {
                                makeSnackText(getString(R.string.invalid_device_name))
                            }
                            else -> {
                                makeSnackText(getString(R.string.error_occurred))
                            }
                        }
                    }
                })
    }

    private fun detachDevice(deviceName: String) {
        ServiceManager.deviceService.detachDeviceFromUser(Vehilytics.user.email, Vehilytics.user.token, deviceName)
                .enqueue(object: VehilyticsCallback<Void>(baseContext) {
                    override fun onResponse(call: Call<Void>?, response: Response<Void>?) {
                        when (response?.code()) {
                            200 -> {
                                device_name_text.text = getString(R.string.none)
                                Vehilytics.clearDevice()
                            }
                            else -> {
                                makeSnackText(getString(R.string.invalid_credentials))
                            }
                        }
                    }
                })
    }
}
