package dev.ymuratov.petai.feature.discover.data.utils

import android.content.Context
import java.io.BufferedReader
import java.io.InputStreamReader

suspend fun readJSONFromAssets(context: Context, path: String): String {
    return try {
        val file = context.assets.open(path)
        val bufferedReader = BufferedReader(InputStreamReader(file))
        val stringBuilder = StringBuilder()
        bufferedReader.useLines { lines ->
            lines.forEach {
                stringBuilder.append(it)
            }
        }
        val jsonString = stringBuilder.toString()
        jsonString
    } catch (e: Exception) {
        e.printStackTrace()
        ""
    }
}