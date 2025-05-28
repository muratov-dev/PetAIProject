package me.yeahapps.mypetai.feature.discover.data.model.dto


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import me.yeahapps.mypetai.feature.discover.data.model.entity.SongEntity
import me.yeahapps.mypetai.feature.discover.data.model.entity.VideoEntity

@Serializable
data class SongDto(
    @SerialName("Name") val name: String,
    @SerialName("Path") val path: String,
    @SerialName("SongCategories") val songCategories: List<String>,
    val url: String,
    val videos: List<VideoDto>
)

fun SongDto.toEntity(id: Int, video: VideoEntity) = SongEntity(
    id = id, name = name, path = path, songCategories = songCategories, url = url, video = video
)