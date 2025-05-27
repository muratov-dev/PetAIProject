package me.yeahapps.mypetai.core.data.network.model.animate_image.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PtInfo(
    @SerialName("audio_url") val audioUrl: String
)
