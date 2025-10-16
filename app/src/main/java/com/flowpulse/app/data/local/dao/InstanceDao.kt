package com.flowpulse.app.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.flowpulse.app.data.local.entity.InstanceEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface InstanceDao {
    @Query("SELECT * FROM instances ORDER BY name")
    fun observeInstances(): Flow<List<InstanceEntity>>

    @Query("SELECT * FROM instances WHERE id = :id")
    suspend fun getById(id: Long): InstanceEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(entity: InstanceEntity): Long

    @Update
    suspend fun update(entity: InstanceEntity)

    @Query("DELETE FROM instances WHERE id = :id")
    suspend fun delete(id: Long)

    @Query("SELECT COUNT(*) FROM instances")
    suspend fun countInstances(): Int
}
