package me.yeahapps.mypetai.feature.profile.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import me.yeahapps.mypetai.feature.profile.domain.model.MyWorkModel

@Entity(tableName = "my_works")
data class MyWorkEntity(
    val title: String, val imageUrl: String, val videoPath: String, @PrimaryKey(autoGenerate = true) val id: Int = 0,
)

fun MyWorkEntity.toDomain() = MyWorkModel(title, imageUrl, videoPath, id)
