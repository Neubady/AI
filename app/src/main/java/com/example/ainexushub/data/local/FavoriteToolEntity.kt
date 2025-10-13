package com.example.ainexushub.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorite_tools")
data class FavoriteToolEntity(
    @PrimaryKey val name: String,
    val description: String,
    val url: String,
    val image: String
)
