package me.yeahapps.mypetai.feature.create.ui.viewmodel

import android.content.Context
import androidx.core.net.toUri
import androidx.lifecycle.SavedStateHandle
import androidx.navigation.toRoute
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import me.yeahapps.mypetai.core.ui.viewmodel.BaseViewModel
import me.yeahapps.mypetai.feature.create.domain.repository.CreateRepository
import me.yeahapps.mypetai.feature.create.ui.action.VideoProcessingAction
import me.yeahapps.mypetai.feature.create.ui.event.VideoProcessingEvent
import me.yeahapps.mypetai.feature.create.ui.screen.VideoProcessingScreen
import me.yeahapps.mypetai.feature.create.ui.state.VideoProcessingState
import java.io.File
import java.io.FileOutputStream
import java.net.URL
import javax.inject.Inject

@HiltViewModel
class VideoProcessingViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val createRepository: CreateRepository
) : BaseViewModel<VideoProcessingState, VideoProcessingEvent, VideoProcessingAction>(VideoProcessingState()) {

    private val args = savedStateHandle.toRoute<VideoProcessingScreen>()

    override fun obtainEvent(viewEvent: VideoProcessingEvent) {}

    init {
        startCreatingVideo()
    }

    private fun startCreatingVideo() = viewModelScoped {
        val imageUrl = createRepository.uploadImage(args.imageUri.toUri())
        val audioUrl = createRepository.uploadImage(args.audioUri.toUri())
        if (imageUrl == null || audioUrl == null) {
            sendAction(VideoProcessingAction.ShowVideoGeneratingError)
            return@viewModelScoped
        }
        val animateImageId = createRepository.animateImage(imageUrl, audioUrl)
        if (animateImageId == null) {
            sendAction(VideoProcessingAction.ShowVideoGeneratingError)
            return@viewModelScoped
        }
        val videoPath = createRepository.getVideoUrl(animateImageId)
        if (videoPath == null) {
            sendAction(VideoProcessingAction.ShowVideoGeneratingError)
            return@viewModelScoped
        }

        updateViewState { copy(videoPath = videoPath) }
    }
}
