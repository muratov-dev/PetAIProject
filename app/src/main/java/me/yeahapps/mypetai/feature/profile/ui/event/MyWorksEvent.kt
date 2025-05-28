package me.yeahapps.mypetai.feature.profile.ui.event

sealed interface MyWorksEvent {

    data object NavigateUp : MyWorksEvent
    data class NavigateToInfo(val id: Long) : MyWorksEvent
}