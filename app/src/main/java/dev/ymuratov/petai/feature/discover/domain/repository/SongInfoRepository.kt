package dev.ymuratov.petai.feature.discover.domain.repository

import dev.ymuratov.petai.feature.discover.domain.model.SongModel

interface SongInfoRepository {

    suspend fun getSongInfo(songId: Int): SongModel
}