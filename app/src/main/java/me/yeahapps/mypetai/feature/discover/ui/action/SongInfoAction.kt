package me.yeahapps.mypetai.feature.discover.ui.action

sealed interface SongInfoAction {
    data object NavigateUp : SongInfoAction
    data class NavigateToVideoProcessing(val songName: String, val imageUri: String, val audioUrl: String) :
        SongInfoAction
}