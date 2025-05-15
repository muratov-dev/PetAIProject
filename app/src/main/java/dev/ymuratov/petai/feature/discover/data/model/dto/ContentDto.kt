package dev.ymuratov.petai.feature.discover.data.model.dto


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ContentDto(
    val images: List<ImageDto>,
    @SerialName("SongCategories")
    val songCategories: List<String>,
    val songs: List<SongDto>,
    val version: Int
)