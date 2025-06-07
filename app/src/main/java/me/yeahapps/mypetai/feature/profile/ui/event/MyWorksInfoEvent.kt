package me.yeahapps.mypetai.feature.profile.ui.event

sealed interface MyWorksInfoEvent {

    data object NavigateUp : MyWorksInfoEvent
    data object DeleteWork : MyWorksInfoEvent
    data object SaveToGallery : MyWorksInfoEvent
}