package me.yeahapps.mypetai.feature.create.ui.viewmodel

import androidx.core.net.toUri
import dagger.hilt.android.lifecycle.HiltViewModel
import me.yeahapps.mypetai.core.ui.viewmodel.BaseViewModel
import me.yeahapps.mypetai.feature.create.data.media.AudioRecorder
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
            is CreateEvent.OnUserImageSelect -> updateViewState { copy(userImageUri = viewEvent.uri) }
            is CreateEvent.OnAudioSelect -> updateViewState { copy(userAudioUri = viewEvent.uri) }
            CreateEvent.DeleteAudio -> updateViewState { copy(userAudioUri = null) }
            CreateEvent.RecordAudio -> sendAction(CreateAction.RecordAudio)
            CreateEvent.CheckRecordedAudio -> updateViewState { copy(userAudioUri = audioRecorder.outputFile?.toUri()) }
            CreateEvent.PlayAudio -> sendAction(CreateAction.PlayAudio)
            CreateEvent.PauseAudio -> sendAction(CreateAction.PauseAudio)
        }
    }
}