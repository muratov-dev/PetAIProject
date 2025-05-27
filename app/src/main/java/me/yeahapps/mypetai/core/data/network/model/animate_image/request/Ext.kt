package me.yeahapps.mypetai.core.data.network.model.animate_image.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Ext(
    @SerialName("sing_title") val singTitle: String = "",
    @SerialName("animate_channel") val animateChannel: String = "phototalk",
    @SerialName("track_info") val trackInfo: String = "{}"
)