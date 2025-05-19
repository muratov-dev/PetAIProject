package dev.ymuratov.petai.feature.discover.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class VideoModel(
    val imagePath: String, val imageUrl: String, val video: String
)