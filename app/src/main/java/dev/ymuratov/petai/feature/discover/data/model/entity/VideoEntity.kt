package dev.ymuratov.petai.feature.discover.data.model.entity

import androidx.room.Embedded
import dev.ymuratov.petai.feature.discover.domain.model.VideoModel
import kotlinx.serialization.Serializable

@Serializable
data class VideoEntity(
    @Embedded val image: ImageEntity, val video: String
)

fun VideoEntity.toDomain() = VideoModel(image.path, image.url, video)