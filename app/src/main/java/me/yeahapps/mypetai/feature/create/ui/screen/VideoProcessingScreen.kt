package me.yeahapps.mypetai.feature.create.ui.screen

import android.view.WindowManager
import android.widget.Toast
import androidx.activity.compose.LocalActivity
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.serialization.Serializable
import me.yeahapps.mypetai.R
import me.yeahapps.mypetai.core.ui.theme.PetAITheme
import me.yeahapps.mypetai.core.ui.utils.collectFlowWithLifecycle
import me.yeahapps.mypetai.feature.create.ui.action.VideoProcessingAction
import me.yeahapps.mypetai.feature.create.ui.component.processing.LoaderSlider
import me.yeahapps.mypetai.feature.create.ui.event.VideoProcessingEvent
import me.yeahapps.mypetai.feature.create.ui.state.VideoProcessingState
import me.yeahapps.mypetai.feature.create.ui.viewmodel.VideoProcessingViewModel
import kotlin.math.roundToInt

@Serializable
data class VideoProcessingScreen(val songName: String = "", val imageUri: String, val audioUri: String)

@Composable
fun VideoProcessingScreenContainer(
    modifier: Modifier = Modifier,
    viewModel: VideoProcessingViewModel = hiltViewModel(),
    navigateUp: () -> Unit,
    navigateToVideo: (String) -> Unit
) {
    val context = LocalContext.current
    val activity = LocalActivity.current
    val state by viewModel.viewState.collectAsStateWithLifecycle()
    viewModel.viewActions.collectFlowWithLifecycle(viewModel) { action ->
        when (action) {
            is VideoProcessingAction.ShowVideoGeneratingError -> {
                navigateUp()
                Toast.makeText(context, "UnknownError", Toast.LENGTH_SHORT).show()
            }

            is VideoProcessingAction.NavigateToVideo -> navigateToVideo(action.videoPath)
            null -> {}
        }
    }

    DisposableEffect(Unit) {
        activity?.window?.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)

        onDispose {
            activity?.window?.clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        }
    }

    VideoProcessingScreenContent(
        modifier = modifier.systemBarsPadding(),
        state = state,
        onEvent = remember { { event -> viewModel.obtainEvent(event) } })
}

@Composable
private fun VideoProcessingScreenContent(
    modifier: Modifier = Modifier,
    state: VideoProcessingState = VideoProcessingState(),
    onEvent: (VideoProcessingEvent) -> Unit = {}
) {
    LaunchedEffect(state.progress) {
        if (state.progress == 1f) {
            state.videoPath?.let {
                onEvent(VideoProcessingEvent.NavigateToVideo(songName = state.songName, videoPath = it))
            }
        }
    }

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(R.drawable.im_loading),
            contentDescription = null,
            modifier = Modifier.size(136.dp)
        )
        Spacer(Modifier.size(16.dp))
        LoaderSlider(
            state.progress, modifier = Modifier
                .padding(horizontal = 60.dp)
                .fillMaxWidth()
        )
        Spacer(Modifier.size(16.dp))
        Text(
            text = "${(state.progress * 100).roundToInt()}%",
            color = PetAITheme.colors.textPrimary,
            style = PetAITheme.typography.textRegular.copy(fontWeight = FontWeight.Medium)
        )
        Spacer(Modifier.size(16.dp))
        Text(
            text = "hold on, wait a minute",
            color = PetAITheme.colors.textPrimary,
            style = PetAITheme.typography.textRegular.copy(fontWeight = FontWeight.Medium)
        )
    }
}