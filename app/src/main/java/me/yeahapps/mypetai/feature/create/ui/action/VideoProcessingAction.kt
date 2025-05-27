package me.yeahapps.mypetai.feature.create.ui.action

sealed interface VideoProcessingAction {

    data object ShowVideoGeneratingError: VideoProcessingAction
}