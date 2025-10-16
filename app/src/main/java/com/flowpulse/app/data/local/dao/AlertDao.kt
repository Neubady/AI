package com.flowpulse.app.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.flowpulse.app.data.local.entity.AlertEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface AlertDao {
    @Query("SELECT * FROM alerts WHERE instance_id = :instanceId ORDER BY created_at DESC")
    fun observe(instanceId: Long): Flow<List<AlertEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entity: AlertEntity): Long

    @Query("DELETE FROM alerts WHERE instance_id = :instanceId")
    suspend fun clear(instanceId: Long)
}
