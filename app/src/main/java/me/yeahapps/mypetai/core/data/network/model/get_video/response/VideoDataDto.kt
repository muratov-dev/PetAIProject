package me.yeahapps.mypetai.core.data.network.model.get_video.response


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class VideoDataDto(
    @SerialName("animate_image_list")
    val videoData: List<VideoDataItemDto>
)