package dev.ymuratov.petai.feature.discover.data.model.dto


import dev.ymuratov.petai.feature.discover.data.model.entity.ImageEntity
import dev.ymuratov.petai.feature.discover.data.model.entity.SongEntity
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SongDto(
    @SerialName("Name") val name: String,
    @SerialName("Path") val path: String,
    @SerialName("SongCategories") val songCategories: List<String>,
    val url: String,
    val videos: List<VideoDto>
)

fun SongDto.toEntity(songId: Int, images: List<ImageEntity>) = SongEntity(
    id = songId,
    name = name,
    path = path,
    songCategories = songCategories,
    url = url,
    videos = videos.map { it.toEntity(images) })