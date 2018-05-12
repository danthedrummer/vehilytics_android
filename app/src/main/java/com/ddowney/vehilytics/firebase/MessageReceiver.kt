package com.ddowney.vehilytics.firebase

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.AudioAttributes
import android.net.Uri
import android.support.v4.app.NotificationCompat
import com.ddowney.vehilytics.R
import com.ddowney.vehilytics.activities.VehicleActivity
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class MessageReceiver: FirebaseMessagingService() {

    companion object {
        private const val REQUEST_CODE = 1
        private const val NOTIFICATION_ID = 6578
        private const val VEHILYTICS_CHANNEL_ID = "VEHILYTICS_CHANNEL_ID"
        private const val VEHILYTICS_CHANNEL_NAME = "VEHICLYTICS_CHANNEL"
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage?) {
        super.onMessageReceived(remoteMessage)

        val title: String = remoteMessage?.data?.get("title") ?: "Empty Title"
        val message: String = remoteMessage?.data?.get("body") ?: "Empty Message"

        showNotifications(title, message)
    }

    private fun showNotifications(title: String, message: String) {
        val intent = Intent(this, VehicleActivity::class.java)

        val pendingIntent = PendingIntent.getActivity(this, REQUEST_CODE,
                intent, PendingIntent.FLAG_UPDATE_CURRENT)

        val notificationSound = Uri.parse("android.resource://${baseContext.packageName}/${R.raw.horn}")

        val notificationBuilder = NotificationCompat.Builder(this, VEHILYTICS_CHANNEL_ID)
                .setContentText(message)
                .setContentTitle(title)
                .setContentIntent(pendingIntent)
                .setSmallIcon(R.drawable.icon_light)
                .setSound(notificationSound)

        val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // This is necessary for notifications to work properly on Oreo
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            val importance = NotificationManager.IMPORTANCE_HIGH
            val notificationChannel = NotificationChannel(VEHILYTICS_CHANNEL_ID,
                    VEHILYTICS_CHANNEL_NAME, importance)
            notificationChannel.enableLights(true)
            notificationChannel.lightColor = R.color.colorPrimary
            notificationChannel.enableVibration(true)
            notificationChannel.vibrationPattern = longArrayOf(100, 200, 100, 200)
            notificationChannel.setSound(notificationSound, AudioAttributes.Builder()
                    .setContentType(AudioAttributes.CONTENT_TYPE_UNKNOWN)
                    .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                    .build())
            notificationBuilder.setChannelId(VEHILYTICS_CHANNEL_ID)
            manager.createNotificationChannel(notificationChannel)
        }

        manager.notify(NOTIFICATION_ID, notificationBuilder.build())
    }
}