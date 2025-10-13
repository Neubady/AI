package com.example.ainexushub.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoriteToolDao {
    @Query("SELECT * FROM favorite_tools")
    fun getFavoriteTools(): Flow<List<FavoriteToolEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTool(tool: FavoriteToolEntity)

    @Delete
    suspend fun deleteTool(tool: FavoriteToolEntity)

    @Query("SELECT EXISTS(SELECT 1 FROM favorite_tools WHERE name = :name)")
    suspend fun isFavorite(name: String): Boolean
}
