package me.yeahapps.mypetai.feature.discover.data.local

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import me.yeahapps.mypetai.feature.discover.data.model.entity.SongCategoryEntity
import me.yeahapps.mypetai.feature.discover.data.model.entity.SongEntity

@Dao
interface DiscoverDao {

    @Query("select * from songs")
    suspend fun getSongs(): List<SongEntity>

    @Query("select * from song_categories")
    suspend fun getCategories(): List<SongCategoryEntity>

    @Query("select * from songs where id = :songId limit 1")
    suspend fun getSongInfo(songId: Int): SongEntity

    @Upsert
    suspend fun saveSongs(songs: List<SongEntity>)

    @Upsert
    suspend fun saveCategories(categories: List<SongCategoryEntity>)
}