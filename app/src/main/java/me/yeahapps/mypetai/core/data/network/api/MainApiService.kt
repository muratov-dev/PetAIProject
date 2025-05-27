package me.yeahapps.mypetai.core.data.network.api

import me.yeahapps.mypetai.core.data.network.model.animate_image.request.AnimateImageRequestDto
import me.yeahapps.mypetai.core.data.network.model.animate_image.response.AnimateImageResponseDto
import me.yeahapps.mypetai.core.data.network.model.get_video.request.GetVideoRequestDto
import me.yeahapps.mypetai.core.data.network.model.get_video.response.GetVideoResponseDto
import me.yeahapps.mypetai.core.data.network.model.upload_file.UploadFileResponseDto
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Query

interface MainApiService {

    @Multipart
    @POST("phone_file_v2/upload_file")
    suspend fun uploadFile(
        @Part file: MultipartBody.Part,
        @Query("account_id") accountId: String = "",
        @Query("app_version") appVersion: String = "5.15.0",
        @Query("language") language: String = "ru",
        @Query("platform_type") platformType: String = "ANDROID",
        @Query("timestamp") timestamp: Long = 1746482384769,
        @Query("token") token: String = "19e86856a8a35da5f938fb53202323ca",
        @Query("user_id") userId: String = "AAFA6D86-8064-4A45-AA20-C612051B73EF"
    ): Response<UploadFileResponseDto>

    @POST("face_v5/animate_image_v5")
    suspend fun animateImage(@Body requestData: AnimateImageRequestDto): Response<AnimateImageResponseDto>

    @POST("reface/animate_image_list_poll")
    suspend fun getVideo(@Body requestData: GetVideoRequestDto): Response<GetVideoResponseDto>
}