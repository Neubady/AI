package com.flowpulse.app.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.flowpulse.app.data.local.entity.QuickActionEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface QuickActionDao {
    @Query("SELECT * FROM quick_actions WHERE instance_id = :instanceId ORDER BY name")
    fun observe(instanceId: Long): Flow<List<QuickActionEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(entity: QuickActionEntity): Long

    @Query("DELETE FROM quick_actions WHERE id = :id")
    suspend fun delete(id: Long)
}
