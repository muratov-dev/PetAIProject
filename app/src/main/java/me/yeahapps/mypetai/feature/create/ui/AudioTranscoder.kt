package me.yeahapps.mypetai.feature.create.ui

import android.media.*

@Suppress("DEPRECATION")
class AudioTranscoder(
    private val inputPath: String,
    private val outputPath: String,
    private val maxDurationSec: Int = 30
) {

    fun transcode(): Boolean {
        val extractor = MediaExtractor()
        extractor.setDataSource(inputPath)

        val trackIndex = (0 until extractor.trackCount).firstOrNull {
            val format = extractor.getTrackFormat(it)
            format.getString(MediaFormat.KEY_MIME)?.startsWith("audio/") == true
        } ?: return false

        extractor.selectTrack(trackIndex)
        val inputFormat = extractor.getTrackFormat(trackIndex)

        val sampleRate = inputFormat.getInteger(MediaFormat.KEY_SAMPLE_RATE)
        val channelCount = inputFormat.getInteger(MediaFormat.KEY_CHANNEL_COUNT)

        val decoder = MediaCodec.createDecoderByType(inputFormat.getString(MediaFormat.KEY_MIME)!!)
        val encoderFormat = MediaFormat.createAudioFormat("audio/mp4a-latm", sampleRate, channelCount).apply {
            setInteger(MediaFormat.KEY_BIT_RATE, 128_000)
            setInteger(MediaFormat.KEY_AAC_PROFILE, MediaCodecInfo.CodecProfileLevel.AACObjectLC)
            setInteger(MediaFormat.KEY_MAX_INPUT_SIZE, 16384)
        }

        val encoder = MediaCodec.createEncoderByType("audio/mp4a-latm")
        decoder.configure(inputFormat, null, null, 0)
        encoder.configure(encoderFormat, null, null, MediaCodec.CONFIGURE_FLAG_ENCODE)

        val muxer = MediaMuxer(outputPath, MediaMuxer.OutputFormat.MUXER_OUTPUT_MPEG_4)

        decoder.start()
        encoder.start()

        val decoderInputBuffers = decoder.inputBuffers
        val decoderOutputBuffers = decoder.outputBuffers
        val encoderInputBuffers = encoder.inputBuffers
        val encoderOutputBuffers = encoder.outputBuffers

        val bufferInfo = MediaCodec.BufferInfo()
        val outputInfo = MediaCodec.BufferInfo()
        var muxerStarted = false
        var audioTrackIndex = -1
        var sawInputEOS = false
        var sawOutputEOS = false
        var presentationTimeUs: Long

        val endTimeUs = maxDurationSec * 1_000_000L

        while (!sawOutputEOS) {
            if (!sawInputEOS) {
                val inputBufferIndex = decoder.dequeueInputBuffer(10000)
                if (inputBufferIndex >= 0) {
                    val inputBuffer = decoderInputBuffers[inputBufferIndex]
                    val sampleSize = extractor.readSampleData(inputBuffer, 0)
                    if (sampleSize < 0 || extractor.sampleTime >= endTimeUs) {
                        decoder.queueInputBuffer(
                            inputBufferIndex, 0, 0, 0,
                            MediaCodec.BUFFER_FLAG_END_OF_STREAM
                        )
                        sawInputEOS = true
                    } else {
                        presentationTimeUs = extractor.sampleTime
                        decoder.queueInputBuffer(
                            inputBufferIndex, 0, sampleSize, presentationTimeUs, 0
                        )
                        extractor.advance()
                    }
                }
            }

            var decoderOutputAvailable = true
            var encoderOutputAvailable = true

            loop@ while (decoderOutputAvailable || encoderOutputAvailable) {
                // Decoder output → Encoder input
                val decoderOutputIndex = decoder.dequeueOutputBuffer(bufferInfo, 10000)
                if (decoderOutputIndex >= 0) {
                    val decodedBuffer = decoderOutputBuffers[decoderOutputIndex]
                    decodedBuffer.position(bufferInfo.offset)
                    decodedBuffer.limit(bufferInfo.offset + bufferInfo.size)

                    val encoderInputIndex = encoder.dequeueInputBuffer(10000)
                    if (encoderInputIndex >= 0) {
                        val inputBuf = encoderInputBuffers[encoderInputIndex]
                        inputBuf.clear()
                        inputBuf.put(decodedBuffer)

                        encoder.queueInputBuffer(
                            encoderInputIndex, 0, bufferInfo.size,
                            bufferInfo.presentationTimeUs,
                            if (bufferInfo.flags and MediaCodec.BUFFER_FLAG_END_OF_STREAM != 0)
                                MediaCodec.BUFFER_FLAG_END_OF_STREAM else 0
                        )
                    }
                    decoder.releaseOutputBuffer(decoderOutputIndex, false)
                } else if (decoderOutputIndex == MediaCodec.INFO_TRY_AGAIN_LATER) {
                    decoderOutputAvailable = false
                }

                // Encoder output → Muxer
                val encoderOutputIndex = encoder.dequeueOutputBuffer(outputInfo, 10000)
                if (encoderOutputIndex >= 0) {
                    val encodedData = encoderOutputBuffers[encoderOutputIndex]
                    encodedData.position(outputInfo.offset)
                    encodedData.limit(outputInfo.offset + outputInfo.size)

                    if (outputInfo.flags and MediaCodec.BUFFER_FLAG_CODEC_CONFIG != 0) {
                        encoder.releaseOutputBuffer(encoderOutputIndex, false)
                        continue@loop
                    }

                    if (!muxerStarted) {
                        val encodedFormat = encoder.outputFormat
                        audioTrackIndex = muxer.addTrack(encodedFormat)
                        muxer.start()
                        muxerStarted = true
                    }

                    muxer.writeSampleData(audioTrackIndex, encodedData, outputInfo)
                    encoder.releaseOutputBuffer(encoderOutputIndex, false)

                    if (outputInfo.flags and MediaCodec.BUFFER_FLAG_END_OF_STREAM != 0) {
                        sawOutputEOS = true
                        break@loop
                    }
                } else if (encoderOutputIndex == MediaCodec.INFO_TRY_AGAIN_LATER) {
                    encoderOutputAvailable = false
                }
            }
        }

        extractor.release()
        decoder.stop()
        decoder.release()
        encoder.stop()
        encoder.release()
        muxer.stop()
        muxer.release()

        return true
    }
}
