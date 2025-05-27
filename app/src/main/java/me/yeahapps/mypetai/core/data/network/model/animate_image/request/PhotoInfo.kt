package me.yeahapps.mypetai.core.data.network.model.animate_image.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PhotoInfo(
    val height: Int = 512,
    @SerialName("mask_path") val maskPath: String = "https://uss3.dreamfaceapp.com/server/aigc/main/b61bae6676364ea6aa012375289d564d.png",
    val width: Int = 332,
    @SerialName("square_face_locations") val squareFaceLocations: List<FaceLocation> = listOf(
        FaceLocation(39, 197, 253, 253)
    ),
    @SerialName("face_nums") val faceNums: Int = 1,
    @SerialName("five_lands") val fiveLands: List<List<List<Int>>> = listOf(
        listOf(
            listOf(117, 288), listOf(214, 288), listOf(166, 343), listOf(136, 374), listOf(195, 374)
        )
    ),
    @SerialName("photo_path") val photoPath: String,
    @SerialName("origin_face_locations") val originFaceLocations: List<FaceLocation> = listOf(
        FaceLocation(39, 197, 253, 253)
    )
)