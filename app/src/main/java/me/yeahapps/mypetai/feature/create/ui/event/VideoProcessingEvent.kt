package me.yeahapps.mypetai.feature.create.ui.event

sealed interface VideoProcessingEvent {
    data class NavigateToVideo(val songName: String = "", val videoPath: String) : VideoProcessingEvent
}