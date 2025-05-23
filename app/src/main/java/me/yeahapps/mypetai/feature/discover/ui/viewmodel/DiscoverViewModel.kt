package me.yeahapps.mypetai.feature.discover.ui.viewmodel

import android.app.Activity
import android.content.Context
import com.android.billingclient.api.BillingClient
import com.android.billingclient.api.Purchase
import com.android.billingclient.api.PurchasesUpdatedListener
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import me.yeahapps.mypetai.core.data.billing.BillingManager
import me.yeahapps.mypetai.core.ui.viewmodel.BaseViewModel
import me.yeahapps.mypetai.feature.discover.domain.model.SongCategoryModel
import me.yeahapps.mypetai.feature.discover.domain.model.SongModel
import me.yeahapps.mypetai.feature.discover.domain.repository.DiscoverRepository
import me.yeahapps.mypetai.feature.discover.ui.action.DiscoverAction
import me.yeahapps.mypetai.feature.discover.ui.action.DiscoverAction.NavigateToSongInfo
import me.yeahapps.mypetai.feature.discover.ui.event.DiscoverEvent
import me.yeahapps.mypetai.feature.discover.ui.state.DiscoverState
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class DiscoverViewModel @Inject constructor(
    private val discoverRepository: DiscoverRepository
) : BaseViewModel<DiscoverState, DiscoverEvent, DiscoverAction>(initialState = DiscoverState()) {

    override fun obtainEvent(viewEvent: DiscoverEvent) {
        when (viewEvent) {
            is DiscoverEvent.SelectCategory -> updateViewState { copy(selectedCategory = viewEvent.category) }
            DiscoverEvent.InitState -> initState()

            is DiscoverEvent.NavigateToSongInfo -> sendAction(NavigateToSongInfo(viewEvent.song))

            is DiscoverEvent.StartSubscription -> {}
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