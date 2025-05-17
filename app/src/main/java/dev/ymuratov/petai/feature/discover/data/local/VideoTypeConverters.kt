package dev.ymuratov.petai.feature.discover.data.local

import androidx.room.TypeConverter
import dev.ymuratov.petai.feature.discover.data.model.entity.VideoEntity
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class VideoTypeConverters {

    @TypeConverter
    fun videoListToJson(value: List<VideoEntity>): String = Json.encodeToString(value)

    @TypeConverter
    fun jsonToVideoList(value: String): List<VideoEntity> = Json.decodeFromString(value)
}