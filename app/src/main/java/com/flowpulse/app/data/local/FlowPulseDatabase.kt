package com.flowpulse.app.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.flowpulse.app.data.local.dao.AlertDao
import com.flowpulse.app.data.local.dao.ExecutionDao
import com.flowpulse.app.data.local.dao.InstanceDao
import com.flowpulse.app.data.local.dao.QuickActionDao
import com.flowpulse.app.data.local.dao.WorkflowDao
import com.flowpulse.app.data.local.entity.AlertEntity
import com.flowpulse.app.data.local.entity.ExecutionEntity
import com.flowpulse.app.data.local.entity.InstanceEntity
import com.flowpulse.app.data.local.entity.QuickActionEntity
import com.flowpulse.app.data.local.entity.WorkflowEntity

@Database(
    entities = [
        InstanceEntity::class,
        WorkflowEntity::class,
        ExecutionEntity::class,
        AlertEntity::class,
        QuickActionEntity::class
    ],
    version = 1,
    exportSchema = true
)
@TypeConverters(RoomTypeConverters::class)
abstract class FlowPulseDatabase : RoomDatabase() {
    abstract fun instanceDao(): InstanceDao
    abstract fun workflowDao(): WorkflowDao
    abstract fun executionDao(): ExecutionDao
    abstract fun alertDao(): AlertDao
    abstract fun quickActionDao(): QuickActionDao
}
