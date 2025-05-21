package me.yeahapps.mypetai.feature.discover.data.model.entity

import kotlinx.serialization.Serializable

@Serializable
data class ImageEntity(
    val path: String, val url: String
)