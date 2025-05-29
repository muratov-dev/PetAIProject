package me.yeahapps.mypetai.feature.discover.data.repository

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import me.yeahapps.mypetai.core.data.network.api.MainApiService
import me.yeahapps.mypetai.core.data.network.model.save_user.SaveUserRequestDto
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
import java.io.BufferedInputStream
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.util.UUID
import java.util.zip.ZipInputStream
import javax.inject.Inject

class DiscoverRepositoryImpl @Inject constructor(
    @ApplicationContext context: Context,
    @ApplicationCoroutineScope coroutineScope: CoroutineScope,
    private val apiService: MainApiService,
    private val discoverDao: DiscoverDao,
    private val preferences: SharedPreferences
) : DiscoverRepository {

    override suspend fun saveUser() {
        val savedUserId = preferences.getString("userId", null)
        if (savedUserId != null) return
        val userId = UUID.randomUUID().toString().uppercase()
        apiService.saveUserId(SaveUserRequestDto(userId = userId))
        preferences.edit {
            putString("userId", userId)
            putBoolean("isFirstLaunch", false)
        }
    }

    init {
        coroutineScope.launch {
            val json = readJSONFromAssets(context, "petContent.json")
            val zipAssetName = "petVideos.zip"
            val zipFile = File(context.filesDir, zipAssetName)
            context.assets.open(zipAssetName).use { inputStream ->
                FileOutputStream(zipFile).use { outputStream ->
                    inputStream.copyTo(outputStream)
                }
            }

            // 2. Распаковываем архив во временную директорию
            val cacheDir = context.cacheDir
            val videos = unzipToCache(zipFile, cacheDir)
            val videosMap = videos.associateBy { it.name }

            val decodedJson = Json.decodeFromString<ContentDto>(json)
            val songs = decodedJson.songs.flatMapIndexed { songId, song ->
                song.videos.mapIndexed { videoId, video ->
                    val filePath = videosMap[video.video]?.absolutePath
                    song.toEntity(songId * 1000 + videoId, video.toEntity(filePath))
                }
            }
            val songCategories = decodedJson.songCategories.mapIndexed { id, name -> SongCategoryEntity(id, name) }

            with(discoverDao) {
                saveSongs(songs.filter { it.video.videoPath != null }.shuffled())
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

    private fun unzipToCache(zipFile: File, cacheDir: File): List<File> {
        val videoFiles = mutableListOf<File>()
        ZipInputStream(BufferedInputStream(FileInputStream(zipFile))).use { zis ->
            var entry = zis.nextEntry
            while (entry != null) {
                if (!entry.isDirectory) {
                    val outFile = File(cacheDir, entry.name)
                    outFile.parentFile?.mkdirs()
                    FileOutputStream(outFile).use { fos ->
                        zis.copyTo(fos)
                    }
                    videoFiles.add(outFile)
                }
                entry = zis.nextEntry
            }
        }
        return videoFiles
    }
}