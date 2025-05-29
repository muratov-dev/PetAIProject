package me.yeahapps.mypetai.feature.create.ui.screen

import android.content.Context
import android.content.Intent
import androidx.annotation.OptIn
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.FileProvider
import androidx.core.net.toUri
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.media3.common.MediaItem
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.AspectRatioFrameLayout
import androidx.media3.ui.PlayerView
import kotlinx.serialization.Serializable
import me.yeahapps.mypetai.R
import me.yeahapps.mypetai.core.ui.component.button.filled.PetAIButtonDefaults
import me.yeahapps.mypetai.core.ui.component.button.filled.PetAIPrimaryButton
import me.yeahapps.mypetai.core.ui.component.button.icon.PetAIIconButton
import me.yeahapps.mypetai.core.ui.component.button.icon.PetAIIconButtonDefaults
import me.yeahapps.mypetai.core.ui.component.topbar.PetAITopAppBar
import me.yeahapps.mypetai.core.ui.component.topbar.PetAITopBarTitleText
import me.yeahapps.mypetai.core.ui.theme.PetAITheme
import me.yeahapps.mypetai.core.ui.utils.collectFlowWithLifecycle
import me.yeahapps.mypetai.core.ui.utils.shareFile
import me.yeahapps.mypetai.feature.create.ui.action.GeneratedVideoAction
import me.yeahapps.mypetai.feature.create.ui.event.GeneratedVideoEvent
import me.yeahapps.mypetai.feature.create.ui.state.GeneratedVideoState
import me.yeahapps.mypetai.feature.create.ui.viewmodel.GeneratedVideoViewModel
import java.io.File

@Serializable
data class GeneratedVideoScreen(val songName: String = "", val videoPath: String)

@Composable
fun GeneratedVideoContainer(
    modifier: Modifier = Modifier, viewModel: GeneratedVideoViewModel = hiltViewModel(), navigateUp: () -> Unit = {}
) {
    val state by viewModel.viewState.collectAsStateWithLifecycle()
    viewModel.viewActions.collectFlowWithLifecycle(viewModel) { action ->
        when (action) {
            GeneratedVideoAction.NavigateUp -> navigateUp()
            null -> {}
        }
    }

    val context = LocalContext.current
    val exoPlayer = remember {
        ExoPlayer.Builder(context).build().apply {
            state.videoPath?.let {
                setMediaItem(MediaItem.fromUri(it.toUri()))
            }
            repeatMode = ExoPlayer.REPEAT_MODE_ALL
            playWhenReady = true
            prepare()
        }
    }

    val lifecycleOwner = LocalLifecycleOwner.current
    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_RESUME -> exoPlayer.play()

                Lifecycle.Event.ON_PAUSE -> exoPlayer.pause()
                else -> {}
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
            exoPlayer.release()
        }
    }

    GeneratedVideoContent(
        modifier = modifier.systemBarsPadding(),
        exoPlayer = exoPlayer,
        state = state,
        onEvent = remember { { event -> viewModel.obtainEvent(event) } })
}

@OptIn(UnstableApi::class)
@Composable
private fun GeneratedVideoContent(
    modifier: Modifier = Modifier,
    exoPlayer: ExoPlayer,
    state: GeneratedVideoState = GeneratedVideoState(),
    onEvent: (GeneratedVideoEvent) -> Unit = {}
) {
    val context = LocalContext.current
    Scaffold(modifier = modifier, topBar = {
        PetAITopAppBar(title = { PetAITopBarTitleText(text = state.songName) }, navigationIcon = {
            PetAIIconButton(
                icon = R.drawable.ic_arrow_left,
                colors = PetAIIconButtonDefaults.colors(containerColor = Color.Transparent),
                onClick = { onEvent(GeneratedVideoEvent.NavigateUp) })
        })
    }) { innerPadding ->
        Column(modifier = Modifier.padding(top = innerPadding.calculateTopPadding(), start = 16.dp, end = 16.dp)) {
            Spacer(Modifier.size(32.dp))
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f / 1.39f, true)
                    .clip(RoundedCornerShape(32.dp))
            ) {
                AndroidView(modifier = Modifier.matchParentSize(), factory = { context ->
                    PlayerView(context).apply {
                        player = exoPlayer
                        useController = false
                        resizeMode = AspectRatioFrameLayout.RESIZE_MODE_FILL
                    }
                })
            }
            Spacer(Modifier.size(60.dp))
            PetAIPrimaryButton(
                centerContent = "Share",
                colors = PetAIButtonDefaults.colors(contentColor = PetAITheme.colors.buttonTextPrimary),
                onClick = { state.videoPath?.let { shareFile(context, File(it), "video/mp4") } },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(Modifier.size(24.dp))
        }
    }
}