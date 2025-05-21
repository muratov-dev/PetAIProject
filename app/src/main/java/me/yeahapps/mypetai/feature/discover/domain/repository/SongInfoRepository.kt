package me.yeahapps.mypetai.feature.discover.domain.repository

import me.yeahapps.mypetai.feature.discover.domain.model.SongModel

interface SongInfoRepository {

    suspend fun getSongInfo(songId: Int): SongModel
}