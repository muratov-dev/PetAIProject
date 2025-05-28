package me.yeahapps.mypetai.feature.discover.data.model.dto


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import me.yeahapps.mypetai.feature.discover.data.model.entity.VideoEntity

@Serializable
data class VideoDto(val image: String, @SerialName("image_url") val imageUrl: String, val video: String)

fun VideoDto.toEntity(filePath: String?) = VideoEntity(image = image, imageUrl = imageUrl, videoPath = filePath)