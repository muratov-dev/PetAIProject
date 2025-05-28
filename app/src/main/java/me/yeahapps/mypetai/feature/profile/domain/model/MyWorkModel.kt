package me.yeahapps.mypetai.feature.profile.domain.model

import me.yeahapps.mypetai.feature.profile.data.model.MyWorkEntity

data class MyWorkModel(
    val title: String,
    val imageUrl: String,
    val videoPath: String,
    val id: Int = 0
)

fun MyWorkModel.toEntity() = MyWorkEntity(title, imageUrl, videoPath, id)
