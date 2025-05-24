package me.yeahapps.mypetai.feature.create.ui.event

sealed interface AudioRecordEvent {

    data object StartRecording : AudioRecordEvent
    data object StopRecording : AudioRecordEvent

    data object NavigateUp : AudioRecordEvent
}