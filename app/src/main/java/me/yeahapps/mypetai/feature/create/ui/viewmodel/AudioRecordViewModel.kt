package me.yeahapps.mypetai.feature.create.ui.viewmodel

import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import me.yeahapps.mypetai.core.ui.viewmodel.BaseViewModel
import me.yeahapps.mypetai.feature.create.data.media.AudioRecorder
import me.yeahapps.mypetai.feature.create.ui.action.AudioRecordAction
import me.yeahapps.mypetai.feature.create.ui.event.AudioRecordEvent
import me.yeahapps.mypetai.feature.create.ui.state.AudioRecordState
import javax.inject.Inject

@HiltViewModel
class AudioRecordViewModel @Inject constructor(
    private val audioRecorder: AudioRecorder
) : BaseViewModel<AudioRecordState, AudioRecordEvent, AudioRecordAction>(initialState = AudioRecordState()) {

    private var audioRecordingJob: Job? = null

    override fun obtainEvent(viewEvent: AudioRecordEvent) {
        when (viewEvent) {
            AudioRecordEvent.StartRecording -> startRecording()
            AudioRecordEvent.StopRecording -> stopRecording()

            AudioRecordEvent.NavigateUp -> cancelRecordingAndNavigateUp()
        }
    }

    private fun startRecording() {
        updateViewState { copy(isRecording = true) }
        audioRecorder.startRecording()
        audioRecordingJob = viewModelScoped {
            while (isActive) {
                val currentDuration = currentState.audioDuration + AUDIO_RECORDING_INTERVAL_MS
                if (currentDuration >= MAX_AUDIO_DURATION) {
                    stopRecording()
                    break
                }
                updateViewState { copy(audioDuration = currentDuration) }
                delay(AUDIO_RECORDING_INTERVAL_MS)
            }
        }
    }

    private fun stopRecording() {
        updateViewState { copy(isRecording = false) }
        audioRecorder.stopRecording()
        sendAction(AudioRecordAction.NavigateUp)
    }

    private fun cancelRecordingAndNavigateUp() {
        audioRecordingJob?.cancel()
        audioRecordingJob = null
        audioRecorder.cancelRecording()
        sendAction(AudioRecordAction.NavigateUp)
    }

    companion object {
        private const val MAX_AUDIO_DURATION = 30_000L
        private const val AUDIO_RECORDING_INTERVAL_MS = 1000L
    }
}