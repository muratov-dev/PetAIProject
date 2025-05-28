package me.yeahapps.mypetai.feature.discover.data.local

import androidx.room.TypeConverter
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import me.yeahapps.mypetai.feature.discover.data.model.entity.VideoEntity

class VideoTypeConverters {

    @TypeConverter
    fun videoToJson(value: VideoEntity): String = Json.encodeToString(value)

    @TypeConverter
    fun jsonToVideo(value: String): VideoEntity = Json.decodeFromString(value)
}