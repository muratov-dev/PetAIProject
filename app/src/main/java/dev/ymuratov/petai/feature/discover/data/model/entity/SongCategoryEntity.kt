package dev.ymuratov.petai.feature.discover.data.model.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import dev.ymuratov.petai.feature.discover.domain.model.SongCategoryModel

@Entity(tableName = "song_categories")
data class SongCategoryEntity(
    @PrimaryKey val id: Int,
    val name: String
)

fun SongCategoryEntity.toDomain() = SongCategoryModel(id, name)
