package com.flowpulse.app.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "instances")
data class InstanceEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
    @ColumnInfo("base_url") val baseUrl: String,
    @ColumnInfo("auth_type") val authType: String,
    @ColumnInfo("encrypted_token") val encryptedToken: String,
    val username: String? = null,
    val encryptedPassword: String? = null,
    @ColumnInfo("developer_mode") val developerMode: Boolean = false,
    @ColumnInfo("push_token") val pushToken: String? = null
)

@Entity(tableName = "workflows")
data class WorkflowEntity(
    @PrimaryKey val id: Long,
    @ColumnInfo("instance_id") val instanceId: Long,
    val name: String,
    val active: Boolean,
    @ColumnInfo("updated_at") val updatedAt: Long?
)

@Entity(tableName = "executions")
data class ExecutionEntity(
    @PrimaryKey val id: Long,
    @ColumnInfo("instance_id") val instanceId: Long,
    @ColumnInfo("workflow_id") val workflowId: Long?,
    val status: String,
    @ColumnInfo("started_at") val startedAt: Long?,
    @ColumnInfo("finished_at") val finishedAt: Long?,
    val error: String? = null,
    val data: String? = null
)

@Entity(tableName = "alerts")
data class AlertEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    @ColumnInfo("instance_id") val instanceId: Long,
    val title: String,
    val message: String,
    @ColumnInfo("created_at") val createdAt: Long,
    val severity: String
)

@Entity(tableName = "quick_actions")
data class QuickActionEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    @ColumnInfo("instance_id") val instanceId: Long,
    val name: String,
    val method: String,
    val url: String,
    val headers: String,
    val body: String?
)
