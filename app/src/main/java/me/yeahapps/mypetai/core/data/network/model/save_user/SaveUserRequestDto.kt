package me.yeahapps.mypetai.core.data.network.model.save_user


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SaveUserRequestDto(
    @SerialName("user_id") val userId: String,
    @SerialName("account_id") val accountId: String = "",
    @SerialName("app_package_name") val appPackageName: String = "DreamFace",
    @SerialName("app_version") val appVersion: String = "5.17.1",
    @SerialName("country_code") val countryCode: String = "BLR",
    @SerialName("device_name") val deviceName: String = "",
    @SerialName("device_system") val deviceSystem: String = "ANDROID",
    @SerialName("install_type") val installType: Int = 66,
    val language: String = "en",
    @SerialName("platform_type") val platformType: String = "ANDROID",
    @SerialName("system_version") val systemVersion: String = "13",
    @SerialName("time_zone") val timeZone: Int = 3,
    val timestamp: Long = System.currentTimeMillis(),
    val token: String = "",
)