package me.yeahapps.mypetai.feature.discover.ui.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.navigation.toRoute
import dagger.hilt.android.lifecycle.HiltViewModel
import me.yeahapps.mypetai.core.ui.viewmodel.BaseViewModel
import me.yeahapps.mypetai.feature.discover.domain.model.DiscoverNavType
import me.yeahapps.mypetai.feature.discover.domain.model.SongModel
import me.yeahapps.mypetai.feature.discover.ui.action.SongInfoAction
import me.yeahapps.mypetai.feature.discover.ui.event.SongInfoEvent
import me.yeahapps.mypetai.feature.discover.ui.screen.SongInfoScreen
import me.yeahapps.mypetai.feature.discover.ui.state.SongInfoState
import javax.inject.Inject
import kotlin.reflect.typeOf

@HiltViewModel
class SongInfoViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle
) : BaseViewModel<SongInfoState, SongInfoEvent, SongInfoAction>(initialState = SongInfoState()) {

    private val args = savedStateHandle.toRoute<SongInfoScreen>(
        typeMap = mapOf(typeOf<SongModel>() to DiscoverNavType.SongType)
    )

    override fun obtainEvent(viewEvent: SongInfoEvent) {
        when (viewEvent) {
            SongInfoEvent.NavigateUp -> sendAction(SongInfoAction.NavigateUp)
            is SongInfoEvent.GenerateVideo -> generateVideo()
        }
    }

    init {
        updateViewState { copy(songInfo = args.song) }
    }

    private fun generateVideo() {

    }
}