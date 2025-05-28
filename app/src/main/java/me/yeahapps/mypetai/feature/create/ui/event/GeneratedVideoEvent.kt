package me.yeahapps.mypetai.feature.create.ui.event

import me.yeahapps.mypetai.feature.create.ui.action.GeneratedVideoAction

sealed interface GeneratedVideoEvent {
    data object NavigateUp : GeneratedVideoEvent
}