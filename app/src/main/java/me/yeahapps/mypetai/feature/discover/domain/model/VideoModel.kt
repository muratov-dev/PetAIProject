package me.yeahapps.mypetai.feature.discover.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class VideoModel(
    val imagePath: String, val imageUrl: String, val video: String
)