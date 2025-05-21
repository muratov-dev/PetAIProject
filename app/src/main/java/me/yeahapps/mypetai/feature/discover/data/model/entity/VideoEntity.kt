package me.yeahapps.mypetai.feature.discover.data.model.entity

import androidx.room.Embedded
import me.yeahapps.mypetai.feature.discover.domain.model.VideoModel
import kotlinx.serialization.Serializable

@Serializable
data class VideoEntity(
    @Embedded val image: ImageEntity, val video: String
)

fun VideoEntity.toDomain() = VideoModel(image.path, image.url, video)