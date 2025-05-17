package dev.ymuratov.petai.feature.discover.data.repository

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import dev.ymuratov.petai.core.di.ApplicationCoroutineScope
import dev.ymuratov.petai.feature.discover.data.local.DiscoverDao
import dev.ymuratov.petai.feature.discover.data.model.dto.ContentDto
import dev.ymuratov.petai.feature.discover.data.model.dto.toEntity
import dev.ymuratov.petai.feature.discover.data.model.entity.SongCategoryEntity
import dev.ymuratov.petai.feature.discover.data.model.entity.toDomain
import dev.ymuratov.petai.feature.discover.data.utils.readJSONFromAssets
import dev.ymuratov.petai.feature.discover.domain.model.SongCategoryModel
import dev.ymuratov.petai.feature.discover.domain.model.SongModel
import dev.ymuratov.petai.feature.discover.domain.repository.DiscoverRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import javax.inject.Inject

class DiscoverRepositoryImpl @Inject constructor(
    @ApplicationContext context: Context,
    @ApplicationCoroutineScope coroutineScope: CoroutineScope,
    private val discoverDao: DiscoverDao
) : DiscoverRepository {
    init {
        coroutineScope.launch {
            val json = readJSONFromAssets(context, "petContent.json")
            val decodedJson = Json.decodeFromString<ContentDto>(json)
            val images = decodedJson.images.map { dto -> dto.toEntity() }
            val songs = decodedJson.songs.mapIndexed { songId, dto -> dto.toEntity(songId, images) }
            val songCategories = decodedJson.songCategories.mapIndexed { id, name -> SongCategoryEntity(id, name) }

            with(discoverDao) {
                saveSongs(songs)
                saveCategories(songCategories)
            }
        }
    }

    override suspend fun getSongs(): List<SongModel> {
        return discoverDao.getSongs().map { song -> song.toDomain() }
    }

    override suspend fun getSongCategories(): List<SongCategoryModel> {
        return discoverDao.getCategories().map { category -> category.toDomain() }
    }
}