package com.example.ainexushub.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [FavoriteToolEntity::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun favoriteToolDao(): FavoriteToolDao
}
