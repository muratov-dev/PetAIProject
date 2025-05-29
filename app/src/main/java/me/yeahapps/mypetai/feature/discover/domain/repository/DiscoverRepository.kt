package me.yeahapps.mypetai.feature.discover.domain.repository

import me.yeahapps.mypetai.feature.discover.domain.model.SongCategoryModel
import me.yeahapps.mypetai.feature.discover.domain.model.SongModel

interface DiscoverRepository {

    suspend fun saveUser()
    suspend fun getSongs(): List<SongModel>
    suspend fun getSongCategories(): List<SongCategoryModel>
}