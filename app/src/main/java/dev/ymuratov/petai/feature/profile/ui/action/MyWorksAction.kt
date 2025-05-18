package dev.ymuratov.petai.feature.profile.ui.action

sealed interface MyWorksAction {
    data object NavigateUp : MyWorksAction
}