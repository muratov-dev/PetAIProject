package me.yeahapps.mypetai.core.ui.utils

import android.content.Context
import android.net.Uri
import java.io.File
import java.io.FileOutputStream

fun copyToCache(context: Context, uri: Uri): File {
    val inputStream = context.contentResolver.openInputStream(uri)!!
    val file = File(context.cacheDir, "input_audio")
    inputStream.use { input -> FileOutputStream(file).use { output -> input.copyTo(output) } }
    return file
}
