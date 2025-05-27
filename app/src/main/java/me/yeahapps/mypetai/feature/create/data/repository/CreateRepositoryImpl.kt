package me.yeahapps.mypetai.feature.create.data.repository

import android.content.Context
import android.net.Uri
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import me.yeahapps.mypetai.core.data.network.api.MainApiService
import me.yeahapps.mypetai.core.data.network.model.animate_image.request.AnimateImageRequestDto
import me.yeahapps.mypetai.core.data.network.model.animate_image.request.PhotoInfo
import me.yeahapps.mypetai.core.data.network.model.animate_image.request.PtInfo
import me.yeahapps.mypetai.core.data.network.model.get_video.request.GetVideoRequestDto
import me.yeahapps.mypetai.core.data.network.utils.asRequestBody
import me.yeahapps.mypetai.core.di.ApplicationCoroutineScope
import me.yeahapps.mypetai.feature.create.domain.repository.CreateRepository
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import timber.log.Timber
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.net.URL
import javax.inject.Inject

class CreateRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context,
    private val mainApiService: MainApiService
) : CreateRepository {

    private val contentResolver = context.contentResolver

    override suspend fun uploadImage(filePath: Uri): String? {
        val mediaType = contentResolver.getType(filePath)
        val fileRequestBody = filePath.asRequestBody(contentResolver, mediaType?.toMediaType())
        val multipartBody = MultipartBody.Part.createFormData("file", "animal.jpg", fileRequestBody)
        return uploadFile(multipartBody)
    }

    override suspend fun uploadAudio(filePath: Uri): String? {
        val mediaType = contentResolver.getType(filePath)
        val fileRequestBody = filePath.asRequestBody(contentResolver, mediaType?.toMediaType())
        val multipartBody = MultipartBody.Part.createFormData("file", "audio.m4a", fileRequestBody)
        return uploadFile(multipartBody)
    }

    private suspend fun uploadFile(multipartBody: MultipartBody.Part): String? {
        try {
            val fileData = mainApiService.uploadFile(file = multipartBody)
            return fileData.body()?.fileData?.filePath
        } catch (ex: Exception) {
            Timber.e(ex)
        }
        return null
    }

    override suspend fun animateImage(imageUrl: String, audioUrl: String): String? {
        try {
            val request = mainApiService.animateImage(
                requestData = AnimateImageRequestDto(
                    ptInfos = listOf(PtInfo(audioUrl)), photoInfoList = listOf(PhotoInfo(photoPath = imageUrl))
                )
            )
            return request.body()?.animateImageData?.animateImageId
        } catch (ex: Exception) {
            Timber.e(ex, "Error animating image")
        }
        return null
    }

    override suspend fun getVideoUrl(token: String): String? {
        try {
            val response = mainApiService.getVideo(GetVideoRequestDto(animateIdList = listOf(token)))
            if (response.body()?.videoData?.videoData?.firstOrNull()?.state == "queue") {
                delay(30000)
                while (true) {
                    val pollResponse = mainApiService.getVideo(GetVideoRequestDto(animateIdList = listOf(token)))
                    if (pollResponse.body()?.videoData?.videoData?.firstOrNull()?.state != "queue") {
                        val videoUrl = pollResponse.body()?.videoData?.videoData?.firstOrNull()?.url
                        val file = saveVideoSilently(videoUrl ?: return null, "$token.mp4")
                        return file?.absolutePath
                    }
                    delay(30_000)
                }
            } else {
                val videoUrl = response.body()?.videoData?.videoData?.firstOrNull()?.url
                val file = saveVideoSilently(videoUrl ?: return null, "$token.mp4")
                return file?.absolutePath
            }
        } catch (ex: Exception) {
            Timber.e(ex, "Error fetching video URL")
        }
        return null
    }

    suspend fun saveVideoSilently(videoUrl: String, fileName: String): File? {
        return withContext(Dispatchers.IO){
            try {
                val url = URL(videoUrl)
                val connection = url.openConnection()
                connection.connect()

                val inputStream = connection.getInputStream()
                val file = File(context.filesDir, fileName) // внутреннее хранилище

                FileOutputStream(file).use { output ->
                    inputStream.copyTo(output)
                }

                file
            } catch (e: Exception) {
                Timber.e(e)
                null
            }
        }
    }
}