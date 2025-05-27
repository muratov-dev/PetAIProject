package me.yeahapps.mypetai.core.data.network.utils

import android.content.ContentResolver
import android.net.Uri
import okhttp3.MediaType
import okhttp3.RequestBody
import okio.BufferedSink
import okio.source


fun Uri.asRequestBody(
    contentResolver: ContentResolver,
    contentType: MediaType? = null,
    contentLength: Long = -1L
) = object : RequestBody() {
    override fun contentType() = contentType

    override fun contentLength(): Long = contentLength

    override fun writeTo(sink: BufferedSink) {
        val input = contentResolver.openInputStream(this@asRequestBody)

        input?.use {
            sink.writeAll(it.source())
        }
    }
}
