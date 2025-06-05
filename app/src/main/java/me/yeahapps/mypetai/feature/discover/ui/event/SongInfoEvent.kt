package me.yeahapps.mypetai.feature.discover.ui.event

sealed interface SongInfoEvent {
    data class GenerateVideo(val songName: String, val imageUri: String, val audioUrl: String) : SongInfoEvent
    data object NavigateUp : SongInfoEvent
}