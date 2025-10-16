package com.flowpulse.app.workers

import android.content.Context
import android.app.NotificationChannel
import android.app.NotificationManager
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import com.flowpulse.app.BuildConfig
import com.flowpulse.app.data.local.AppPreferences
import com.flowpulse.app.domain.repo.FlowPulseRepository
import com.flowpulse.app.notifications.FlowPulseFirebaseMessagingService
import com.flowpulse.app.R
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.first
import java.util.concurrent.TimeUnit

@HiltWorker
class ErrorPollingWorker @AssistedInject constructor(
    @Assisted appContext: Context,
    @Assisted workerParams: WorkerParameters,
    private val repository: FlowPulseRepository,
    private val preferences: AppPreferences
) : CoroutineWorker(appContext, workerParams) {

    override suspend fun doWork(): Result {
        val instances = repository.observeInstances().first()
        instances.forEach { instance ->
            val lastKnown = preferences.lastErrorExecutionId(instance.id).first()
            repository.refreshExecutions(instance.id, status = "error")
            val updated = preferences.lastErrorExecutionId(instance.id).first()
            if (updated > lastKnown) {
                createChannelIfNeeded()
                notifyError(instance.name, updated)
            }
        }
        return Result.success()
    }

    private fun createChannelIfNeeded() {
        val manager = applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (manager.getNotificationChannel(FlowPulseFirebaseMessagingService.CHANNEL_ALERTS) == null) {
            val channel = NotificationChannel(
                FlowPulseFirebaseMessagingService.CHANNEL_ALERTS,
                applicationContext.getString(R.string.notification_channel_alerts),
                NotificationManager.IMPORTANCE_HIGH
            )
            manager.createNotificationChannel(channel)
        }
    }

    private fun notifyError(instanceName: String, executionId: Long) {
        val notification = NotificationCompat.Builder(applicationContext, FlowPulseFirebaseMessagingService.CHANNEL_ALERTS)
            .setSmallIcon(R.drawable.ic_notification)
            .setContentTitle(applicationContext.getString(R.string.notification_default_title))
            .setContentText("$instanceName #$executionId")
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .build()
        NotificationManagerCompat.from(applicationContext).notify(executionId.toInt(), notification)
    }

    companion object {
        private const val UNIQUE_NAME = "flowpulse_error_polling"

        fun schedule(context: Context) {
            val request = PeriodicWorkRequestBuilder<ErrorPollingWorker>(
                BuildConfig.POLL_INTERVAL_MINUTES.toLong(), TimeUnit.MINUTES
            ).build()
            WorkManager.getInstance(context).enqueueUniquePeriodicWork(
                UNIQUE_NAME,
                ExistingPeriodicWorkPolicy.UPDATE,
                request
            )
        }
    }
}
