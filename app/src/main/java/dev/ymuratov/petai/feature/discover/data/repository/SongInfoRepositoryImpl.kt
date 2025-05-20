package dev.ymuratov.petai.feature.discover.data.repository

import dev.ymuratov.petai.feature.discover.data.local.DiscoverDao
import dev.ymuratov.petai.feature.discover.data.model.entity.toDomain
import dev.ymuratov.petai.feature.discover.domain.model.SongModel
import dev.ymuratov.petai.feature.discover.domain.repository.SongInfoRepository
import javax.inject.Inject

class SongInfoRepositoryImpl @Inject constructor(
    private val dao: DiscoverDao
) : SongInfoRepository {

    override suspend fun getSongInfo(songId: Int): SongModel {
        return dao.getSongInfo(songId).toDomain()
    }
}