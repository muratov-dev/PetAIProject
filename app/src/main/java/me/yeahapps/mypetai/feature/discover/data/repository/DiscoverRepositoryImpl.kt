package me.yeahapps.mypetai.feature.discover.data.repository

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import me.yeahapps.mypetai.core.di.ApplicationCoroutineScope
import me.yeahapps.mypetai.feature.discover.data.local.DiscoverDao
import me.yeahapps.mypetai.feature.discover.data.model.dto.ContentDto
import me.yeahapps.mypetai.feature.discover.data.model.dto.toEntity
import me.yeahapps.mypetai.feature.discover.data.model.entity.SongCategoryEntity
import me.yeahapps.mypetai.feature.discover.data.model.entity.toDomain
import me.yeahapps.mypetai.feature.discover.data.utils.readJSONFromAssets
import me.yeahapps.mypetai.feature.discover.domain.model.SongCategoryModel
import me.yeahapps.mypetai.feature.discover.domain.model.SongModel
import me.yeahapps.mypetai.feature.discover.domain.repository.DiscoverRepository
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