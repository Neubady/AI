package com.flowpulse.app.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.flowpulse.app.data.local.entity.WorkflowEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface WorkflowDao {
    @Query("SELECT * FROM workflows WHERE instance_id = :instanceId ORDER BY name")
    fun observe(instanceId: Long): Flow<List<WorkflowEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertAll(entities: List<WorkflowEntity>)

    @Query("UPDATE workflows SET active = :active WHERE id = :workflowId AND instance_id = :instanceId")
    suspend fun updateActive(instanceId: Long, workflowId: Long, active: Boolean)

    @Query("DELETE FROM workflows WHERE instance_id = :instanceId")
    suspend fun clear(instanceId: Long)

    @Query("SELECT * FROM workflows WHERE instance_id = :instanceId")
    suspend fun getOnce(instanceId: Long): List<WorkflowEntity>
}
