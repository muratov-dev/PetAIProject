package me.yeahapps.mypetai.feature.profile.ui.action

sealed interface MyWorksAction {
    data object NavigateUp : MyWorksAction
    data class NavigateToInfo(val id: Long) : MyWorksAction
}