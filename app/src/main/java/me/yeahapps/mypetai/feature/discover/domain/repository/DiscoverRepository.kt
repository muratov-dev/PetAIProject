package me.yeahapps.mypetai.feature.discover.domain.repository

import kotlinx.coroutines.flow.Flow
import me.yeahapps.mypetai.feature.discover.domain.model.SongCategoryModel
import me.yeahapps.mypetai.feature.discover.domain.model.SongModel

interface DiscoverRepository {

    suspend fun saveUser()
    fun getSongs(): Flow<List<SongModel>>
    fun getSongCategories(): Flow<List<SongCategoryModel>>
}