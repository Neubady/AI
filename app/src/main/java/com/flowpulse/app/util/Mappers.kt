package com.flowpulse.app.util

import com.flowpulse.app.data.local.entity.ExecutionEntity
import com.flowpulse.app.data.local.entity.InstanceEntity
import com.flowpulse.app.data.local.entity.QuickActionEntity
import com.flowpulse.app.data.local.entity.WorkflowEntity
import com.flowpulse.app.data.local.entity.AlertEntity
import com.flowpulse.app.data.remote.ExecutionDto
import com.flowpulse.app.data.remote.WorkflowDto
import com.flowpulse.app.domain.model.AuthType
import com.flowpulse.app.domain.model.Execution
import com.flowpulse.app.domain.model.ExecutionError
import com.flowpulse.app.domain.model.N8nInstance
import com.flowpulse.app.domain.model.QuickAction
import com.flowpulse.app.domain.model.Alert
import com.flowpulse.app.domain.model.AlertSeverity
import com.flowpulse.app.domain.model.Workflow
import kotlinx.datetime.Instant
import kotlinx.datetime.toInstant

fun WorkflowDto.toEntity(instanceId: Long): WorkflowEntity = WorkflowEntity(
    id = id,
    instanceId = instanceId,
    name = name,
    active = active,
    updatedAt = updatedAt?.let { runCatching { Instant.parse(it) }.getOrNull()?.toEpochMilliseconds() }
)

fun WorkflowEntity.toDomain(): Workflow = Workflow(
    id = id,
    name = name,
    active = active,
    updatedAt = updatedAt?.let { Instant.fromEpochMilliseconds(it) }
)

fun ExecutionDto.toEntity(instanceId: Long): ExecutionEntity = ExecutionEntity(
    id = id,
    instanceId = instanceId,
    workflowId = workflowId,
    status = status ?: "unknown",
    startedAt = startedAt?.let { runCatching { Instant.parse(it) }.getOrNull()?.toEpochMilliseconds() },
    finishedAt = stoppedAt?.let { runCatching { Instant.parse(it) }.getOrNull()?.toEpochMilliseconds() },
    error = error?.message,
    data = data?.toString()
)

fun ExecutionEntity.toDomain(): Execution = Execution(
    id = id,
    workflowId = workflowId ?: -1,
    status = status,
    startedAt = startedAt?.let { Instant.fromEpochMilliseconds(it) },
    finishedAt = finishedAt?.let { Instant.fromEpochMilliseconds(it) },
    error = error?.let { ExecutionError(message = it, stack = null) },
    data = emptyMap()
)

fun InstanceEntity.toDomain(token: String): N8nInstance = N8nInstance(
    id = id,
    name = name,
    baseUrl = baseUrl,
    authType = AuthType.valueOf(authType),
    apiKey = token,
    username = username,
    password = null,
    developerMode = developerMode
)

fun N8nInstance.toEntity(encryptedToken: String, encryptedPassword: String?): InstanceEntity = InstanceEntity(
    id = id,
    name = name,
    baseUrl = baseUrl,
    authType = authType.name,
    encryptedToken = encryptedToken,
    username = username,
    encryptedPassword = encryptedPassword,
    developerMode = developerMode
)

fun QuickActionEntity.toDomain(): QuickAction = QuickAction(
    id = id,
    instanceId = instanceId,
    name = name,
    method = method,
    url = url,
    headers = runCatching { headers.split("\n").associate {
        val parts = it.split(":", limit = 2)
        parts[0] to parts.getOrElse(1) { "" }.trim()
    } }.getOrDefault(emptyMap()),
    body = body
)

fun QuickAction.toEntity(): QuickActionEntity = QuickActionEntity(
    id = id,
    instanceId = instanceId,
    name = name,
    method = method,
    url = url,
    headers = headers.entries.joinToString("\n") { "${it.key}:${it.value}" },
    body = body
)

fun AlertEntity.toDomain(): Alert = Alert(
    id = id,
    instanceId = instanceId,
    title = title,
    message = message,
    createdAt = Instant.fromEpochMilliseconds(createdAt),
    severity = runCatching { AlertSeverity.valueOf(severity) }.getOrElse { AlertSeverity.INFO }
)

fun Alert.toEntity(): AlertEntity = AlertEntity(
    id = id,
    instanceId = instanceId,
    title = title,
    message = message,
    createdAt = createdAt.toEpochMilliseconds(),
    severity = severity.name
)
