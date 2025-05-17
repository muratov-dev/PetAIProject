package dev.ymuratov.petai.feature.discover.ui.event

import dev.ymuratov.petai.feature.discover.domain.model.SongCategoryModel

sealed interface DiscoverEvent {
    data class SelectCategory(val category: SongCategoryModel?) : DiscoverEvent
    data object InitState : DiscoverEvent
}