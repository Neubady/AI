package com.flowpulse.app.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "executions")
data class ExecutionEntity(
    @PrimaryKey val id: Long,
    val instanceId: Long,
    val workflowId: Long?,
    val status: String,
    val startedAt: String?,
    val stoppedAt: String?,
    val error: String?
)
