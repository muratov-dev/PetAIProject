package dev.ymuratov.petai.feature.profile.ui.event

sealed interface MyWorksEvent {

    data object NavigateUp : MyWorksEvent
}