package me.yeahapps.mypetai.feature.create.ui.state

data class VideoProcessingState(
    val progress: Float = 0f, val songName: String = "", val videoPath: String? = null
)
