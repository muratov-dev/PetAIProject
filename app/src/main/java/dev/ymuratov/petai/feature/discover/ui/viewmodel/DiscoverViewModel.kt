package dev.ymuratov.petai.feature.discover.ui.viewmodel

import dagger.hilt.android.lifecycle.HiltViewModel
import dev.ymuratov.petai.core.ui.viewmodel.BaseViewModel
import dev.ymuratov.petai.feature.discover.domain.model.SongCategoryModel
import dev.ymuratov.petai.feature.discover.domain.model.SongModel
import dev.ymuratov.petai.feature.discover.domain.repository.DiscoverRepository
import dev.ymuratov.petai.feature.discover.ui.action.DiscoverAction
import dev.ymuratov.petai.feature.discover.ui.event.DiscoverEvent
import dev.ymuratov.petai.feature.discover.ui.state.DiscoverState
import javax.inject.Inject

@HiltViewModel
class DiscoverViewModel @Inject constructor(
    private val discoverRepository: DiscoverRepository
) : BaseViewModel<DiscoverState, DiscoverEvent, DiscoverAction>(initialState = DiscoverState()) {

    override fun obtainEvent(viewEvent: DiscoverEvent) {
        when (viewEvent) {
            is DiscoverEvent.SelectCategory -> updateViewState { copy(selectedCategory = viewEvent.category) }
            DiscoverEvent.InitState -> initState()

            is DiscoverEvent.NavigateToSongInfo -> sendAction(DiscoverAction.NavigateToSongInfo(viewEvent.song))
        }
    }

    private fun initState() = viewModelScoped {
        val songs = discoverRepository.getSongs().flatMap { song ->
            song.videos.mapIndexed { idx, video ->
                SongModel(
                    id = song.id * 1000 + idx,
                    name = song.name,
                    videos = listOf(video),
                    path = song.path,
                    songCategories = song.songCategories,
                    url = song.url
                )
            }
        }

        val categories = discoverRepository.getSongCategories()
        val bottomSheetCategories = songs.flatMap { it.songCategories }.distinct()
        val bottomSheetCategoriesMapped = bottomSheetCategories.mapIndexed { index, category ->
            SongCategoryModel(index, category)
        }
        updateViewState {
            copy(songs = songs, songCategories = categories, bottomSheetCategories = bottomSheetCategoriesMapped)
        }
    }
}