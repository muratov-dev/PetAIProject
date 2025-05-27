package me.yeahapps.mypetai.feature.create.ui.viewmodel

import android.content.Context
import androidx.core.net.toUri
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import me.yeahapps.mypetai.core.ui.viewmodel.BaseViewModel
import me.yeahapps.mypetai.feature.create.data.media.AudioRecorder
import me.yeahapps.mypetai.feature.create.domain.repository.CreateRepository
import me.yeahapps.mypetai.feature.create.ui.action.CreateAction
import me.yeahapps.mypetai.feature.create.ui.event.CreateEvent
import me.yeahapps.mypetai.feature.create.ui.state.CreateState
import javax.inject.Inject

@HiltViewModel
class CreateViewModel @Inject constructor(
    private val audioRecorder: AudioRecorder
) : BaseViewModel<CreateState, CreateEvent, CreateAction>(initialState = CreateState()) {

    override fun obtainEvent(viewEvent: CreateEvent) {
        when (viewEvent) {
            is CreateEvent.OnUserImageSelect -> {
                updateViewState { copy(userImageUri = viewEvent.uri) }
                checkIsButtonEnabled()
            }

            is CreateEvent.OnAudioSelect -> {
                updateViewState { copy(userAudioUri = viewEvent.uri) }
                checkIsButtonEnabled()
            }

            CreateEvent.DeleteAudio -> {
                updateViewState { copy(userAudioUri = null) }
                checkIsButtonEnabled()
            }

            CreateEvent.RecordAudio -> sendAction(CreateAction.RecordAudio)
            CreateEvent.CheckRecordedAudio -> {
                updateViewState { copy(userAudioUri = audioRecorder.outputFile?.toUri()) }
                checkIsButtonEnabled()
            }

            CreateEvent.PlayAudio -> sendAction(CreateAction.PlayAudio)
            CreateEvent.PauseAudio -> sendAction(CreateAction.PauseAudio)
            CreateEvent.StartCreatingVideo -> {
                val imageUri = currentState.userImageUri ?: return
                val audioUri = currentState.userAudioUri ?: return
                sendAction(CreateAction.StartCreatingVideo(imageUri.toString(), audioUri.toString()))
            }
        }
    }

    private fun checkIsButtonEnabled() {
        val isButtonEnabled = currentState.userImageUri != null && currentState.userAudioUri != null
        updateViewState { copy(isButtonEnabled = isButtonEnabled) }
    }
}