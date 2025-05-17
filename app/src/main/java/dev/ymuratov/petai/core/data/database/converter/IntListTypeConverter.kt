package online.meditorium.core.data.database.converter

import androidx.room.TypeConverter
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class IntListTypeConverter {

    @TypeConverter
    fun fromIntList(value: List<Int>): String = Json.encodeToString(value)

    @TypeConverter
    fun toIntList(value: String): List<Int> = Json.decodeFromString(value)
}