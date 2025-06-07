package me.yeahapps.mypetai.feature.discover.ui.viewmodel

import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import me.yeahapps.mypetai.core.ui.viewmodel.BaseViewModel
import me.yeahapps.mypetai.feature.discover.domain.model.SongCategoryModel
import me.yeahapps.mypetai.feature.discover.domain.repository.DiscoverRepository
import me.yeahapps.mypetai.feature.discover.ui.action.DiscoverAction
import me.yeahapps.mypetai.feature.discover.ui.action.DiscoverAction.*
import me.yeahapps.mypetai.feature.discover.ui.event.DiscoverEvent
import me.yeahapps.mypetai.feature.discover.ui.screen.DiscoverContainer
import me.yeahapps.mypetai.feature.discover.ui.state.DiscoverState
import javax.inject.Inject

@HiltViewModel
class DiscoverViewModel @Inject constructor(
    private val discoverRepository: DiscoverRepository
) : BaseViewModel<DiscoverState, DiscoverEvent, DiscoverAction>(initialState = DiscoverState()) {

    override fun obtainEvent(viewEvent: DiscoverEvent) {
        when (viewEvent) {
            is DiscoverEvent.SelectCategory -> updateViewState { copy(selectedCategory = viewEvent.category) }

            is DiscoverEvent.NavigateToSongInfo -> sendAction(NavigateToSongInfo(viewEvent.song))
            DiscoverEvent.NavigateToCreate -> sendAction(DiscoverAction.NavigateToCreate)
            DiscoverEvent.NavigateToSubscriptions -> sendAction(DiscoverAction.NavigateToSubscriptions)
        }
    }

    init {
        viewModelScoped {
            discoverRepository.saveUser()
        }
        viewModelScoped {
            discoverRepository.getSongs().collectLatest { songs ->
                val bottomSheetCategories = songs.flatMap { it.songCategories }.distinct()
                val bottomSheetCategoriesMapped = bottomSheetCategories.mapIndexed { index, category ->
                    SongCategoryModel(index, category)
                }
                updateViewState { copy(songs = songs, bottomSheetCategories = bottomSheetCategoriesMapped) }
            }
        }
        viewModelScoped {
            discoverRepository.getSongCategories().collectLatest { categories ->
                updateViewState { copy(songCategories = categories) }
            }
        }
    }
}