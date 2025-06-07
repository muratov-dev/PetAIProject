package me.yeahapps.mypetai.feature.create.ui.event

import android.net.Uri

sealed interface CreateEvent {

    data class OnUserImageSelect(val uri: Uri) : CreateEvent
    data class OnAudioSelect(val uri: Uri) : CreateEvent

    data object RecordAudio : CreateEvent
    data object DeleteAudio : CreateEvent
    data object CheckRecordedAudio : CreateEvent

    data object PlayAudio : CreateEvent
    data object PauseAudio : CreateEvent

    data object StartCreatingVideo: CreateEvent
    data object NavigateToSubscriptions: CreateEvent
}