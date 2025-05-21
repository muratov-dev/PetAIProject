package me.yeahapps.mypetai.core.data.database.converter

import androidx.room.TypeConverter
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class StringListTypeConverter {

    @TypeConverter
    fun fromIntList(value: List<String>): String = Json.encodeToString(value)

    @TypeConverter
    fun toIntList(value: String): List<String> = Json.decodeFromString(value)
}