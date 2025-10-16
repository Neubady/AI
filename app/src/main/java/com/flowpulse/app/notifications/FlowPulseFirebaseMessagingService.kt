package com.flowpulse.app.notifications

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationManagerCompat
import com.flowpulse.app.R
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class FlowPulseFirebaseMessagingService : FirebaseMessagingService() {

    @Inject
    lateinit var notificationBuilder: FlowPulseNotificationBuilder

    override fun onCreate() {
        super.onCreate()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                getString(R.string.default_notification_channel_id),
                getString(R.string.default_notification_channel_name),
                NotificationManager.IMPORTANCE_HIGH
            )
            val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            manager.createNotificationChannel(channel)
        }
    }

    override fun onMessageReceived(message: RemoteMessage) {
        val notification = notificationBuilder.buildFromRemoteMessage(this, message).build()
        NotificationManagerCompat.from(this).notify(message.messageId?.hashCode() ?: 0, notification)
    }

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        // Persist token using repository / secure storage
    }
}
