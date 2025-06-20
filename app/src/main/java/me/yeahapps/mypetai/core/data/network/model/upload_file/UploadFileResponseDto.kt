package me.yeahapps.mypetai.core.data.network.model.upload_file


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UploadFileResponseDto(
    @SerialName("data") val fileData: FileDataDto?,
    @SerialName("status_code") val statusCode: String,
    @SerialName("status_msg") val statusMsg: String
)