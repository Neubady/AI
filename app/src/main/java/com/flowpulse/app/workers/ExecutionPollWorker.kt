package com.flowpulse.app.workers

import android.content.Context
import androidx.core.app.NotificationManagerCompat
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.flowpulse.app.notifications.FlowPulseNotificationBuilder
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

@HiltWorker
class ExecutionPollWorker @AssistedInject constructor(
    @Assisted appContext: Context,
    @Assisted params: WorkerParameters,
    private val notificationBuilder: FlowPulseNotificationBuilder
) : CoroutineWorker(appContext, params) {

    override suspend fun doWork(): Result {
        // TODO: Real implementation fetches remote executions and compares with cache
        val notification = notificationBuilder
            .buildLocalAlert(
                context = applicationContext,
                title = "Errores detectados",
                description = "Se encontraron fallos recientes en n8n",
                data = mapOf("destination" to "executions/0")
            )
            .build()
        NotificationManagerCompat.from(applicationContext).notify(1001, notification)
        return Result.success()
    }
}
