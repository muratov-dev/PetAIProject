package dev.ymuratov.petai.feature.discover.ui.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.navigation.toRoute
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.ymuratov.petai.core.ui.viewmodel.BaseViewModel
import dev.ymuratov.petai.feature.discover.domain.model.DiscoverNavType
import dev.ymuratov.petai.feature.discover.domain.model.SongModel
import dev.ymuratov.petai.feature.discover.ui.action.SongInfoAction
import dev.ymuratov.petai.feature.discover.ui.event.SongInfoEvent
import dev.ymuratov.petai.feature.discover.ui.screen.SongInfoScreen
import dev.ymuratov.petai.feature.discover.ui.state.SongInfoState
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

    }

    init {
        updateViewState { copy(songInfo = args.song) }
    }
}