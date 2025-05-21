package me.yeahapps.mypetai.feature.discover.data.model.dto


import me.yeahapps.mypetai.feature.discover.data.model.entity.ImageEntity
import me.yeahapps.mypetai.feature.discover.data.model.entity.VideoEntity
import kotlinx.serialization.Serializable

@Serializable
data class VideoDto(
    val image: String,
    val video: String
)

fun VideoDto.toEntity(images: List<ImageEntity>): VideoEntity {
    val image = images.find { it.path == this.image } ?: ImageEntity("", "")
    return VideoEntity(image, video)
}