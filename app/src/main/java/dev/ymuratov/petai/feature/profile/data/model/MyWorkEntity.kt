package dev.ymuratov.petai.feature.profile.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import dev.ymuratov.petai.feature.profile.domain.model.MyWorkModel

@Entity(tableName = "my_works")
data class MyWorkEntity(
    @PrimaryKey val id: Int, val title: String, val imageUrl: String, val videoUrl: String
)

fun MyWorkEntity.toDomain() = MyWorkModel(id, title, imageUrl, videoUrl)
