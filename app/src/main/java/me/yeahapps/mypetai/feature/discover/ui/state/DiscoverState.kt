package me.yeahapps.mypetai.feature.discover.ui.state

import me.yeahapps.mypetai.feature.discover.domain.model.SongCategoryModel
import me.yeahapps.mypetai.feature.discover.domain.model.SongModel

data class DiscoverState(
    val hasSubscription: Boolean = false,
    val songs: List<SongModel> = emptyList(),
    val songCategories: List<SongCategoryModel> = emptyList(),
    val bottomSheetCategories: List<SongCategoryModel> = emptyList(),
    val selectedCategory: SongCategoryModel? = null,
)
