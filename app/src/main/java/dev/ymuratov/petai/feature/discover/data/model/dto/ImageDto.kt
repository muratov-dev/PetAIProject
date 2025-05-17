package dev.ymuratov.petai.feature.discover.data.model.dto


import dev.ymuratov.petai.feature.discover.data.model.entity.ImageEntity
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ImageDto(
    @SerialName("Path")
    val path: String,
    val url: String
)

fun ImageDto.toEntity() = ImageEntity(path, url)