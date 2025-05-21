package me.yeahapps.mypetai.feature.profile.ui.event

sealed interface MyWorksEvent {

    data object NavigateUp : MyWorksEvent
}