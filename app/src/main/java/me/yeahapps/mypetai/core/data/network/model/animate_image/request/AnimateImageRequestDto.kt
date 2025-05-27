package me.yeahapps.mypetai.core.data.network.model.animate_image.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AnimateImageRequestDto(
    @SerialName("play_types") val playTypes: List<String> = listOf("ANYTHING", "PT"),
    val token: String = "",
    @SerialName("task_id") val taskId: String = "D5509443-163B-47D8-9CB7-6FACA48A17B6",
    @SerialName("app_version") val appVersion: String = "5.15.0",
    val timestamp: Long = 1746481251270,
    @SerialName("trace_id") val traceId: String = "",
    @SerialName("aigc_img_no_save_flag") val aigcImgNoSaveFlag: Boolean = true,
    @SerialName("no_water_mark") val noWaterMark: Int = 1,
    @SerialName("platform_type") val platformType: String = "APPLE",
    @SerialName("template_id") val templateId: String = "655b213cccd1db0007e1d977",
    @SerialName("account_id") val accountId: String = "",
    val ext: Ext = Ext(),
    @SerialName("merge_by_server") val mergeByServer: Boolean = false,
    @SerialName("user_id") val userId: String = "AAFA6D86-8064-4A45-AA20-C612051B73EF",
    val language: String = "ru",
    @SerialName("pt_infos") val ptInfos: List<PtInfo>,
    @SerialName("photo_info_list") val photoInfoList: List<PhotoInfo>
)