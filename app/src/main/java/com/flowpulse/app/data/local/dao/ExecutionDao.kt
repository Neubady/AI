package com.flowpulse.app.data.local.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.flowpulse.app.data.local.entity.ExecutionEntity

@Dao
interface ExecutionDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(executions: List<ExecutionEntity>)

    @Query("SELECT * FROM executions WHERE instanceId = :instanceId ORDER BY id DESC")
    fun pagingSource(instanceId: Long): PagingSource<Int, ExecutionEntity>

    @Query("DELETE FROM executions WHERE instanceId = :instanceId")
    suspend fun clear(instanceId: Long)
}
