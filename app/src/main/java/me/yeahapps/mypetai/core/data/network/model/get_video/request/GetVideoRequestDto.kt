package me.yeahapps.mypetai.core.data.network.model.get_video.request


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GetVideoRequestDto(
    @SerialName("user_id") val userId: String,
    @SerialName("account_id") val accountId: String = "",
    @SerialName("animate_id_list") val animateIdList: List<String>,
    @SerialName("app_version") val appVersion: String = "5.15.0",
    val language: String = "ru",
    @SerialName("platform_type") val platformType: String = "ANDROID",
    val timestamp: Long = System.currentTimeMillis(),
    val token: String = "4eaece80bc1375f2cec2d7ff724f7b50"
)