package me.yeahapps.mypetai.feature.discover.ui.state

import androidx.compose.runtime.Immutable
import me.yeahapps.mypetai.feature.discover.domain.model.SongModel

@Immutable
data class SongInfoState(
    val songInfo: SongModel? = null
)
