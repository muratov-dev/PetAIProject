package me.yeahapps.mypetai.feature.create.ui

import android.content.Context
import android.media.MediaCodec
import android.media.MediaExtractor
import android.media.MediaFormat
import android.media.MediaMuxer
import android.net.Uri
import java.io.File
import java.nio.ByteBuffer

object AudioTrimmer {
    fun getDurationUs(file: File): Long {
        val extractor = MediaExtractor()
        extractor.setDataSource(file.absolutePath)
        val format = extractor.getTrackFormat(0)
        val duration = format.getLong(MediaFormat.KEY_DURATION)
        extractor.release()
        return duration
    }

    fun trimTo30Seconds(inputFile: File, outputFile: File, maxDurationUs: Long = 30_000_000L) {
        val extractor = MediaExtractor()
        extractor.setDataSource(inputFile.absolutePath)
        extractor.selectTrack(0)
        val format = extractor.getTrackFormat(0)

        val muxer = MediaMuxer(outputFile.absolutePath, MediaMuxer.OutputFormat.MUXER_OUTPUT_MPEG_4)
        val dstIndex = muxer.addTrack(format)
        muxer.start()

        val buffer = ByteBuffer.allocate(1024 * 1024)
        val bufferInfo = MediaCodec.BufferInfo()

        extractor.seekTo(0, MediaExtractor.SEEK_TO_CLOSEST_SYNC)

        while (true) {
            bufferInfo.offset = 0
            bufferInfo.size = extractor.readSampleData(buffer, 0)
            if (bufferInfo.size < 0) break

            bufferInfo.presentationTimeUs = extractor.sampleTime
            if (bufferInfo.presentationTimeUs > maxDurationUs) break

            bufferInfo.flags = extractor.sampleFlags
            muxer.writeSampleData(dstIndex, buffer, bufferInfo)
            extractor.advance()
        }

        muxer.stop()
        muxer.release()
        extractor.release()
    }

    fun uriToFile(context: Context, uri: Uri, filename: String): File {
        val file = File(context.cacheDir, filename)
        context.contentResolver.openInputStream(uri)?.use { input ->
            file.outputStream().use { output -> input.copyTo(output) }
        }
        return file
    }
}