package dev.ymuratov.petai.feature.discover.ui.action

import dev.ymuratov.petai.feature.discover.domain.model.SongModel

sealed interface DiscoverAction {
    data class NavigateToSongInfo(val song: SongModel) : DiscoverAction
}