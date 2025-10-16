package com.flowpulse.app.data.local

import androidx.room.ProvidedTypeConverter
import androidx.room.TypeConverter
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

@ProvidedTypeConverter
class RoomTypeConverters(private val json: Json) {

    @TypeConverter
    fun fromStringMap(value: String?): Map<String, String>? = value?.let { json.decodeFromString(it) }

    @TypeConverter
    fun toStringMap(value: Map<String, String>?): String? = value?.let { json.encodeToString(it) }

    @TypeConverter
    fun fromString(value: String?): Map<String, Any?>? = value?.let { json.decodeFromString(it) }

    @TypeConverter
    fun toString(value: Map<String, Any?>?): String? = value?.let { json.encodeToString(it) }
}
