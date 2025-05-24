package me.yeahapps.mypetai.feature.create.ui.state

data class AudioRecordState(
    val isRecording: Boolean = false,
    val audioDuration: Long = 0L,
)
