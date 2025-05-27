package me.yeahapps.mypetai.core.data.network.model.get_video.response


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class VideoDataItemDto(
    val state: String,
    val url: String? = null,
)