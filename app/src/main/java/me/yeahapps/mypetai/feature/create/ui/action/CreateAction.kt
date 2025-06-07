package me.yeahapps.mypetai.feature.create.ui.action

sealed interface CreateAction {

    data object PlayAudio : CreateAction
    data object PauseAudio : CreateAction

    data object RecordAudio : CreateAction
    data object NavigateToSubscriptions : CreateAction

    data class StartCreatingVideo(val imageUri: String, val audioUri: String) : CreateAction
}