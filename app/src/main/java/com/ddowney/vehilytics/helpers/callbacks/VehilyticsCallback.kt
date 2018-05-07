package com.ddowney.vehilytics.helpers.callbacks

import android.content.Context
import android.content.Intent
import android.util.Log
import com.ddowney.vehilytics.Vehilytics
import com.ddowney.vehilytics.activities.LoginActivity
import com.ddowney.vehilytics.storage.Storage
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

abstract class VehilyticsCallback<T>(val context: Context): Callback<T> {

    companion object {
        private const val LOG_TAG = "VehilyticsCallback"
    }

    override fun onFailure(call: Call<T>?, t: Throwable?) {
        Log.e(LOG_TAG, "Error: ${t?.message}")
    }

    override fun onResponse(call: Call<T>?, response: Response<T>?) {
        if (response?.code() == 401) {
            Vehilytics.clearAll(Storage(context))
            val intent = Intent(context, LoginActivity::class.java)
            intent.addFlags (Intent.FLAG_ACTIVITY_NEW_TASK
                    or Intent.FLAG_ACTIVITY_CLEAR_TOP
                    or Intent.FLAG_ACTIVITY_CLEAR_TASK)
            context.startActivity(intent)
        }
    }
}