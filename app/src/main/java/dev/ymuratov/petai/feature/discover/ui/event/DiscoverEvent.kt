package dev.ymuratov.petai.feature.discover.ui.event

import dev.ymuratov.petai.feature.discover.domain.model.SongCategoryModel
import dev.ymuratov.petai.feature.discover.domain.model.SongModel
import dev.ymuratov.petai.feature.discover.ui.action.DiscoverAction

sealed interface DiscoverEvent {
    data class SelectCategory(val category: SongCategoryModel?) : DiscoverEvent
    data object InitState : DiscoverEvent

    data class NavigateToSongInfo(val song: SongModel) : DiscoverEvent
}