package dev.ymuratov.petai.feature.discover.ui.state

import dev.ymuratov.petai.feature.discover.domain.model.SongCategoryModel
import dev.ymuratov.petai.feature.discover.domain.model.SongModel

data class DiscoverState(
    val isLoading: Boolean = true,
    val songs: List<SongModel> = emptyList(),
    val songCategories: List<SongCategoryModel> = emptyList()
)
