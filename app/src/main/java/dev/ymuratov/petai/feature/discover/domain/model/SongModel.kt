package dev.ymuratov.petai.feature.discover.domain.model


data class SongModel(
    val id: Int,
    val name: String,
    val path: String,
    val songCategories: List<String>,
    val url: String,
    val videos: List<VideoModel>
)