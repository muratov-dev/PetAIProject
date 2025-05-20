package dev.ymuratov.petai.feature.discover.ui.state

import androidx.compose.runtime.Immutable
import dev.ymuratov.petai.feature.discover.domain.model.SongModel

@Immutable
data class SongInfoState(
    val songInfo: SongModel? = null
)
