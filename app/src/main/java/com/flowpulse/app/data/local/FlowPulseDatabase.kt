package com.flowpulse.app.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.flowpulse.app.data.local.dao.ExecutionDao
import com.flowpulse.app.data.local.entity.ExecutionEntity

@Database(
    entities = [ExecutionEntity::class],
    version = 1,
    exportSchema = true
)
abstract class FlowPulseDatabase : RoomDatabase() {
    abstract fun executionDao(): ExecutionDao
}
