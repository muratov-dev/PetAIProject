package me.yeahapps.mypetai.feature.create.ui.viewmodel

import androidx.core.net.toUri
import androidx.lifecycle.SavedStateHandle
import androidx.navigation.toRoute
import dagger.hilt.android.lifecycle.HiltViewModel
import me.yeahapps.mypetai.core.ui.viewmodel.BaseViewModel
import me.yeahapps.mypetai.feature.create.domain.repository.CreateRepository
import me.yeahapps.mypetai.feature.create.ui.action.VideoProcessingAction
import me.yeahapps.mypetai.feature.create.ui.event.VideoProcessingEvent
import me.yeahapps.mypetai.feature.create.ui.screen.VideoProcessingScreen
import me.yeahapps.mypetai.feature.create.ui.state.VideoProcessingState
import me.yeahapps.mypetai.feature.profile.domain.model.MyWorkModel
import me.yeahapps.mypetai.feature.profile.domain.repository.MyWorksRepository
import javax.inject.Inject

@HiltViewModel
class VideoProcessingViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val createRepository: CreateRepository,
    private val myWorksRepository: MyWorksRepository
) : BaseViewModel<VideoProcessingState, VideoProcessingEvent, VideoProcessingAction>(VideoProcessingState()) {

    private val args = savedStateHandle.toRoute<VideoProcessingScreen>()

    override fun obtainEvent(viewEvent: VideoProcessingEvent) {
        when (viewEvent) {
            is VideoProcessingEvent.NavigateToVideo -> sendAction(
                VideoProcessingAction.NavigateToVideo(songName = viewEvent.songName, videoPath = viewEvent.videoPath)
            )
        }
    }

    init {
        updateViewState { copy(songName = args.songName) }
        startCreatingVideo()
    }

    private fun startCreatingVideo() = viewModelScoped {
        val imageUrl = createRepository.uploadImage(args.imageUri.toUri())
        updateViewState { copy(progress = (currentState.progress + 0.25f)) }
        val audioUrl = if (args.audioUri.startsWith("http")) {
            args.audioUri
        } else {
            createRepository.uploadAudio(args.audioUri.toUri())
        }
        updateViewState { copy(progress = (currentState.progress + 0.25f)) }
        if (imageUrl == null || audioUrl == null) {
            sendAction(VideoProcessingAction.ShowVideoGeneratingError)
            return@viewModelScoped
        }
        val animateImageId = createRepository.animateImage(imageUrl, audioUrl)
        updateViewState { copy(progress = (currentState.progress + 0.25f)) }
        if (animateImageId == null) {
            sendAction(VideoProcessingAction.ShowVideoGeneratingError)
            return@viewModelScoped
        }
        val videoPath = createRepository.getVideoUrl(animateImageId)
        if (videoPath == null) {
            sendAction(VideoProcessingAction.ShowVideoGeneratingError)
            return@viewModelScoped
        }
        myWorksRepository.createMyWork(
            MyWorkModel(
                title = if (args.songName.isEmpty()) "My pet $animateImageId" else args.songName,
                imageUrl = imageUrl,
                videoPath = videoPath
            )
        )

        updateViewState { copy(videoPath = videoPath, progress = 1f) }
    }
}
