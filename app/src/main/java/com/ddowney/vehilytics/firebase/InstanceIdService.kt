package com.ddowney.vehilytics.firebase

import android.util.Log
import com.ddowney.vehilytics.Vehilytics
import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.iid.FirebaseInstanceIdService

class InstanceIdService: FirebaseInstanceIdService() {

    companion object {
        private const val LOG_TAG = "FirebaseLogs"
    }

    override fun onTokenRefresh() {
        super.onTokenRefresh()
        val firebaseToken: String = FirebaseInstanceId.getInstance().token ?: ""
        Log.d(LOG_TAG, "Saving firebase token")
        Vehilytics.firebaseToken = firebaseToken
    }
}