package me.yeahapps.mypetai.feature.discover.ui.action

import me.yeahapps.mypetai.feature.discover.domain.model.SongModel

sealed interface DiscoverAction {
    data class NavigateToSongInfo(val song: SongModel) : DiscoverAction

    data object NavigateToCreate : DiscoverAction
}