package me.yeahapps.mypetai.feature.discover.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class SongModel(
    val id: Int,
    val name: String,
    val path: String,
    val songCategories: List<String>,
    val url: String,
    val videos: List<VideoModel>
)