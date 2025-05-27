package me.yeahapps.mypetai.feature.create.domain.repository

import android.net.Uri

interface CreateRepository {

    suspend fun uploadImage(filePath: Uri): String?
    suspend fun uploadAudio(filePath: Uri): String?

    suspend fun animateImage(imageUrl: String, audioUrl: String): String?

    suspend fun getVideoUrl(token: String): String?
}