package com.flowpulse.app.notifications

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import com.flowpulse.app.MainActivity
import com.flowpulse.app.R
import com.google.firebase.messaging.RemoteMessage
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FlowPulseNotificationBuilder @Inject constructor() {
    fun buildFromRemoteMessage(context: Context, message: RemoteMessage): NotificationCompat.Builder =
        createBaseBuilder(context, message.notification?.title, message.notification?.body, message.data)

    fun buildLocalAlert(
        context: Context,
        title: String,
        description: String,
        data: Map<String, String> = emptyMap()
    ): NotificationCompat.Builder = createBaseBuilder(context, title, description, data)

    private fun createBaseBuilder(
        context: Context,
        title: String?,
        body: String?,
        data: Map<String, String>
    ): NotificationCompat.Builder {
        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
            data["instance"]?.let { putExtra("instance", it) }
            data["executionId"]?.let { putExtra("executionId", it) }
        }
        val pendingIntent = PendingIntent.getActivity(
            context,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        return NotificationCompat.Builder(context, context.getString(R.string.default_notification_channel_id))
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentTitle(title ?: context.getString(R.string.app_name))
            .setContentText(body ?: "")
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
    }
}
