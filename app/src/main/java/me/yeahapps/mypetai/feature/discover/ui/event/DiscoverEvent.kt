package me.yeahapps.mypetai.feature.discover.ui.event

import me.yeahapps.mypetai.feature.discover.domain.model.SongCategoryModel
import me.yeahapps.mypetai.feature.discover.domain.model.SongModel

sealed interface DiscoverEvent {
    data class SelectCategory(val category: SongCategoryModel?) : DiscoverEvent

    data class NavigateToSongInfo(val song: SongModel) : DiscoverEvent
    data object NavigateToCreate : DiscoverEvent
}