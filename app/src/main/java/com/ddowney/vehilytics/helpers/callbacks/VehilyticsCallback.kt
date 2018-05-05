package com.ddowney.vehilytics.helpers.callbacks

import android.util.Log
import retrofit2.Call
import retrofit2.Callback

abstract class VehilyticsCallback<T>: Callback<T> {

    companion object {
        private const val LOG_TAG = "VehilyticsCallback"
    }

    override fun onFailure(call: Call<T>?, t: Throwable?) {
        Log.e(LOG_TAG, "Error: ${t?.message}")
    }
}