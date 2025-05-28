package me.yeahapps.mypetai.feature.create.ui.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.navigation.toRoute
import dagger.hilt.android.lifecycle.HiltViewModel
import me.yeahapps.mypetai.core.ui.viewmodel.BaseViewModel
import me.yeahapps.mypetai.feature.create.ui.action.GeneratedVideoAction
import me.yeahapps.mypetai.feature.create.ui.event.GeneratedVideoEvent
import me.yeahapps.mypetai.feature.create.ui.screen.GeneratedVideoScreen
import me.yeahapps.mypetai.feature.create.ui.state.GeneratedVideoState
import javax.inject.Inject

@HiltViewModel
class GeneratedVideoViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle
) : BaseViewModel<GeneratedVideoState, GeneratedVideoEvent, GeneratedVideoAction>(GeneratedVideoState()) {

    val args = savedStateHandle.toRoute<GeneratedVideoScreen>()

    override fun obtainEvent(viewEvent: GeneratedVideoEvent) {
        when (viewEvent) {
            GeneratedVideoEvent.NavigateUp -> sendAction(GeneratedVideoAction.NavigateUp)
        }
    }

    init {
        updateViewState { copy(songName = args.songName, videoPath = args.videoPath) }
    }
}