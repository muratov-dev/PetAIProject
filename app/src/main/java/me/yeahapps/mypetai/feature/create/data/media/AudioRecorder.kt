package me.yeahapps.mypetai.feature.create.data.media

import android.content.Context
import android.media.MediaRecorder
import android.os.Build
import dagger.hilt.android.qualifiers.ApplicationContext
import timber.log.Timber
import java.io.File
import javax.inject.Inject
import javax.inject.Singleton

@Suppress("DEPRECATION")
@Singleton
class AudioRecorder @Inject constructor(@ApplicationContext private val context: Context) {
    private var recorder: MediaRecorder? = null
    var outputFile: File? = null

    fun startRecording() {
        outputFile = File(context.cacheDir, "recorded_${System.currentTimeMillis()}.m4a")
        Timber.d("startRecording")
        recorder = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) MediaRecorder(context) else MediaRecorder()

        recorder?.apply {
            setAudioSource(MediaRecorder.AudioSource.MIC)
            setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
            setAudioEncoder(MediaRecorder.AudioEncoder.AAC)
            setAudioEncodingBitRate(128_000)        // 128 kbps — нормальный битрейт
            setAudioSamplingRate(44100)             // 44.1 kHz — классика для аудио
            setOutputFile(outputFile?.absolutePath)
            prepare()
            start()
        }
    }

    fun stopRecording() {
        Timber.d("stopRecording")
        recorder?.apply {
            stop()
            release()
        }
        recorder = null
    }

    fun cancelRecording() {
        Timber.d("cancelRecording")
        recorder?.apply {
            stop()
            release()
        }
        recorder = null
        if (outputFile?.exists() == true) {
            outputFile?.delete()
        }
    }
}