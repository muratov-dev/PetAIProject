package me.yeahapps.mypetai.core.data.network.model.animate_image.response


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AnimateImageDataDto(
    @SerialName("animate_image_id") val animateImageId: String,
    @SerialName("animate_limited") val animateLimited: Boolean,
    @SerialName("demo_animate") val demoAnimate: Boolean,
    @SerialName("merge_by_server") val mergeByServer: Boolean
)