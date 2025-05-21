package me.yeahapps.mypetai.feature.discover.ui.event

import android.app.Activity
import androidx.activity.result.ActivityResult
import me.yeahapps.mypetai.feature.discover.domain.model.SongCategoryModel
import me.yeahapps.mypetai.feature.discover.domain.model.SongModel

sealed interface DiscoverEvent {
    data class SelectCategory(val category: SongCategoryModel?) : DiscoverEvent
    data object InitState : DiscoverEvent

    data class StartSubscription(val activity: Activity): DiscoverEvent

    data class NavigateToSongInfo(val song: SongModel) : DiscoverEvent
}