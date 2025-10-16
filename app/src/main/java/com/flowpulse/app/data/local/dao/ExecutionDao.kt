package com.flowpulse.app.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.flowpulse.app.data.local.entity.ExecutionEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ExecutionDao {
    @Query("SELECT * FROM executions WHERE instance_id = :instanceId ORDER BY started_at DESC")
    fun observe(instanceId: Long): Flow<List<ExecutionEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertAll(entities: List<ExecutionEntity>)

    @Query("SELECT * FROM executions WHERE id = :executionId LIMIT 1")
    suspend fun getById(executionId: Long): ExecutionEntity?

    @Query("DELETE FROM executions WHERE id = :executionId")
    suspend fun delete(executionId: Long)

    @Query("DELETE FROM executions WHERE instance_id = :instanceId")
    suspend fun clear(instanceId: Long)

    @Query("SELECT * FROM executions WHERE instance_id = :instanceId")
    suspend fun getByInstance(instanceId: Long): List<ExecutionEntity>
}
