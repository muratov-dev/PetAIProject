package me.yeahapps.mypetai.feature.discover.data.model.entity


import androidx.room.Entity
import androidx.room.PrimaryKey
import me.yeahapps.mypetai.feature.discover.domain.model.SongModel

@Entity(tableName = "songs")
data class SongEntity(
    @PrimaryKey val id: Int,
    val name: String,
    val path: String,
    val songCategories: List<String>,
    val url: String,
    val videos: List<VideoEntity>
)

fun SongEntity.toDomain() = SongModel(id, name, path, songCategories, url, videos.map { it.toDomain() })