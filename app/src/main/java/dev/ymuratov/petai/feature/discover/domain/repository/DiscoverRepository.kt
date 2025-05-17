package dev.ymuratov.petai.feature.discover.domain.repository

import dev.ymuratov.petai.feature.discover.domain.model.SongCategoryModel
import dev.ymuratov.petai.feature.discover.domain.model.SongModel

interface DiscoverRepository {

    suspend fun getSongs(): List<SongModel>
    suspend fun getSongCategories(): List<SongCategoryModel>
}