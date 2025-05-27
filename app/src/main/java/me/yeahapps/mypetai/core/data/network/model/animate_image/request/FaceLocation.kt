package me.yeahapps.mypetai.core.data.network.model.animate_image.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class FaceLocation(
    @SerialName("left_upper_x") val leftUpperX: Int,
    @SerialName("left_upper_y") val leftUpperY: Int,
    @SerialName("right_width") val rightWidth: Int,
    @SerialName("down_high") val downHigh: Int
)