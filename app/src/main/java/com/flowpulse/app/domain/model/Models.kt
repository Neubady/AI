package com.flowpulse.app.domain.model

import kotlinx.datetime.Instant
import kotlinx.serialization.Serializable

enum class AuthType { BEARER, BASIC }

data class N8nInstance(
    val id: Long,
    val name: String,
    val baseUrl: String,
    val authType: AuthType,
    val apiKey: String,
    val username: String? = null,
    val password: String? = null,
    val pushNotificationsEnabled: Boolean = false,
    val developerMode: Boolean = false
)

@Serializable
data class Workflow(
    val id: Long,
    val name: String,
    val active: Boolean,
    val updatedAt: Instant?
)

@Serializable
data class Execution(
    val id: Long,
    val workflowId: Long,
    val status: String,
    val startedAt: Instant?,
    val finishedAt: Instant?,
    val error: ExecutionError? = null,
    val data: Map<String, Any?> = emptyMap()
)

@Serializable
data class ExecutionError(
    val message: String?,
    val stack: String?
)

@Serializable
data class Alert(
    val id: Long,
    val instanceId: Long,
    val title: String,
    val message: String,
    val createdAt: Instant,
    val severity: AlertSeverity
)

enum class AlertSeverity { INFO, WARNING, ERROR }

@Serializable
data class QuickAction(
    val id: Long,
    val instanceId: Long,
    val name: String,
    val method: String,
    val url: String,
    val headers: Map<String, String> = emptyMap(),
    val body: String? = null
)

data class InstanceMetrics(
    val activeWorkflows: Int,
    val executionsToday: Int,
    val errorsToday: Int,
    val averageLatencyMs: Long,
    val chartPoints: List<MetricsPoint>
)

@Serializable
data class MetricsPoint(
    val timestamp: Instant,
    val value: Int
)
