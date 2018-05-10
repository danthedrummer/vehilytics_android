package com.ddowney.vehilytics.firebase

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.support.v4.app.NotificationCompat
import com.ddowney.vehilytics.R
import com.ddowney.vehilytics.activities.HomeActivity
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class MessageReceiver: FirebaseMessagingService() {

    companion object {
        private const val REQUEST_CODE = 1
        private const val NOTIFICATION_ID = 6578
        private const val VEHILYTICS_CHANNEL_ID = "VEHILYTICS_CHANNEL_ID"
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage?) {
        super.onMessageReceived(remoteMessage)

        val title: String = remoteMessage?.data?.get("title") ?: "Empty Title"
        val message: String = remoteMessage?.data?.get("body") ?: "Empty Message"

        showNotifications(title, message)
    }

    private fun showNotifications(title: String, message: String) {
        val intent = Intent(this, HomeActivity::class.java)

        val pendingIntent = PendingIntent.getActivity(this, REQUEST_CODE,
                intent, PendingIntent.FLAG_UPDATE_CURRENT)

        val notification = NotificationCompat.Builder(this, VEHILYTICS_CHANNEL_ID)
                .setContentText(message)
                .setContentTitle(title)
                .setContentIntent(pendingIntent)
                .setSmallIcon(R.drawable.icon_primary)
                .setSound(Uri.parse("android.resource://${baseContext.packageName}/${R.raw.horn}"))
                .build()

        val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        manager.notify(NOTIFICATION_ID, notification)
    }
}