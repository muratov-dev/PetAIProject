package me.yeahapps.mypetai.feature.discover.data.model.entity

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import me.yeahapps.mypetai.feature.discover.domain.model.VideoModel

@Serializable
data class VideoEntity(val image: String, val imageUrl: String, @SerialName("video") val videoPath: String?)

fun VideoEntity.toDomain() = VideoModel(image, imageUrl, videoPath)