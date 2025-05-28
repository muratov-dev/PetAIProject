package me.yeahapps.mypetai.feature.discover.data.model.dto


import kotlinx.serialization.Serializable

@Serializable
data class ContentDto(
    val songCategories: List<String>,
    val songs: List<SongDto>,
    val version: Int
)