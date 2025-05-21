package me.yeahapps.mypetai.feature.discover.domain.model

import android.net.Uri
import android.os.Bundle
import androidx.navigation.NavType
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

object DiscoverNavType {

    val SongType = object : NavType<SongModel>(
        isNullableAllowed = false
    ) {
        override fun get(bundle: Bundle, key: String): SongModel? {
            return Json.Default.decodeFromString(bundle.getString(key) ?: return null)
        }

        override fun parseValue(value: String): SongModel {
            return Json.Default.decodeFromString(Uri.decode(value))
        }

        override fun serializeAsValue(value: SongModel): String {
            return Uri.encode(Json.Default.encodeToString(value))
        }

        override fun put(bundle: Bundle, key: String, value: SongModel) {
            bundle.putString(key, Json.Default.encodeToString(value))
        }
    }
}