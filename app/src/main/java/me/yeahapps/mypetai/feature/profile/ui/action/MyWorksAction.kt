package me.yeahapps.mypetai.feature.profile.ui.action

sealed interface MyWorksAction {
    data object NavigateUp : MyWorksAction
}