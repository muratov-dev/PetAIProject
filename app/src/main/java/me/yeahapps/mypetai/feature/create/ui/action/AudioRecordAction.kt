package me.yeahapps.mypetai.feature.create.ui.action


sealed interface AudioRecordAction {
    data object NavigateUp : AudioRecordAction
}