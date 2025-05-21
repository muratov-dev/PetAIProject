package me.yeahapps.mypetai.feature.discover.data.repository

import me.yeahapps.mypetai.feature.discover.data.local.DiscoverDao
import me.yeahapps.mypetai.feature.discover.data.model.entity.toDomain
import me.yeahapps.mypetai.feature.discover.domain.model.SongModel
import me.yeahapps.mypetai.feature.discover.domain.repository.SongInfoRepository
import javax.inject.Inject

class SongInfoRepositoryImpl @Inject constructor(
    private val dao: DiscoverDao
) : SongInfoRepository {

    override suspend fun getSongInfo(songId: Int): SongModel {
        return dao.getSongInfo(songId).toDomain()
    }
}