package dev.ymuratov.petai.feature.discover.ui.event

sealed interface DiscoverEvent {

    data object InitState : DiscoverEvent
}