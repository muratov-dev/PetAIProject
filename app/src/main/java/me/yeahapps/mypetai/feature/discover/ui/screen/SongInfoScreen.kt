package me.yeahapps.mypetai.feature.discover.ui.screen

import android.Manifest
import android.content.Context
import android.net.Uri
import android.os.Environment
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.OptIn
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.FileProvider
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.media3.common.MediaItem
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import kotlinx.serialization.Serializable
import me.yeahapps.mypetai.R
import me.yeahapps.mypetai.core.ui.component.PetAIVideoPlayer
import me.yeahapps.mypetai.core.ui.component.bottomsheet.ImageSourceSelectorBottomSheet
import me.yeahapps.mypetai.core.ui.component.button.filled.PetAIButtonDefaults
import me.yeahapps.mypetai.core.ui.component.button.filled.PetAIPrimaryButton
import me.yeahapps.mypetai.core.ui.component.button.filled.PetAITertiaryButton
import me.yeahapps.mypetai.core.ui.component.button.icon.PetAIIconButton
import me.yeahapps.mypetai.core.ui.component.button.icon.PetAIIconButtonDefaults
import me.yeahapps.mypetai.core.ui.component.topbar.PetAITopAppBar
import me.yeahapps.mypetai.core.ui.component.topbar.PetAITopBarTitleText
import me.yeahapps.mypetai.core.ui.navigation.commonModifier
import me.yeahapps.mypetai.core.ui.theme.PetAITheme
import me.yeahapps.mypetai.core.ui.utils.ManagePlayerLifecycle
import me.yeahapps.mypetai.core.ui.utils.collectFlowWithLifecycle
import me.yeahapps.mypetai.feature.discover.domain.model.SongModel
import me.yeahapps.mypetai.feature.discover.ui.action.SongInfoAction
import me.yeahapps.mypetai.feature.discover.ui.event.SongInfoEvent
import me.yeahapps.mypetai.feature.discover.ui.state.SongInfoState
import me.yeahapps.mypetai.feature.discover.ui.viewmodel.SongInfoViewModel
import me.yeahapps.mypetai.feature.subscription.ui.screen.SubscriptionsContainer
import timber.log.Timber
import java.io.File

@Serializable
data class SongInfoScreen(val song: SongModel)

@Composable
fun SongInfoContainer(
    modifier: Modifier = Modifier,
    viewModel: SongInfoViewModel = hiltViewModel(),
    navigateUp: () -> Unit = {},
    navigateToSubscriptions: () -> Unit = {},
    navigateToProcessing: (String, String, String) -> Unit
) {
    val state by viewModel.viewState.collectAsStateWithLifecycle()

    viewModel.viewActions.collectFlowWithLifecycle(viewModel) { action ->
        when (action) {
            SongInfoAction.NavigateUp -> navigateUp()
            is SongInfoAction.NavigateToVideoProcessing -> {
                navigateToProcessing(action.songName, action.imageUri, action.audioUrl)
            }

            null -> {}
        }
    }

    val context = LocalContext.current
    val exoPlayer = remember {
        ExoPlayer.Builder(context).build().apply {
            state.songInfo?.let {
                it.video.videoPath?.let { uri -> setMediaItem(MediaItem.fromUri(uri)) }
            }
            repeatMode = ExoPlayer.REPEAT_MODE_ALL
            playWhenReady = true
            prepare()
        }
    }


    ManagePlayerLifecycle(exoPlayer)
    SongInfoContent(
        modifier = modifier.systemBarsPadding(),
        exoPlayer = exoPlayer,
        state = state,
        onEvent = remember { { event -> viewModel.obtainEvent(event) } },
    )
}

@OptIn(UnstableApi::class)
@kotlin.OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SongInfoContent(
    modifier: Modifier = Modifier,
    exoPlayer: ExoPlayer,
    state: SongInfoState = SongInfoState(),
    onEvent: (SongInfoEvent) -> Unit = {},
) {
    var isSubscriptionsScreenVisible by remember { mutableStateOf(false) }
    val context = LocalContext.current
    val sheetState = rememberModalBottomSheetState()
    //TODO Почистить переменные, вынести в VM
    var uria by remember { mutableStateOf<Uri?>(null) }
    var isMuted by remember { mutableStateOf(false) }
    var currentVolume by remember { mutableFloatStateOf(0f) }
    var avatarSourceSelectionVisible by remember { mutableStateOf(false) }

    //TODO Вынести, потому что переиспользуется, сделать обработку отказа
    val pickMedia = rememberLauncherForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
        if (uri != null) {
            avatarSourceSelectionVisible = false
            uria = uri
        } else Timber.d("No media selected")
    }

    val photoUri: Uri = FileProvider.getUriForFile(context, "${context.packageName}.provider", createImageFile(context))
    val cameraLauncher = rememberLauncherForActivityResult(ActivityResultContracts.TakePicture()) { success ->
        if (success) {
            avatarSourceSelectionVisible = false
            uria = photoUri
        } else Timber.d("No media selected")
    }

    val cameraPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(), onResult = { isGranted ->
            if (isGranted) cameraLauncher.launch(photoUri)
        })


    Scaffold(modifier = modifier, topBar = {
        PetAITopAppBar(title = {
            state.songInfo?.let { PetAITopBarTitleText(text = it.name) }
        }, navigationIcon = {
            PetAIIconButton(
                icon = R.drawable.ic_arrow_left,
                colors = PetAIIconButtonDefaults.colors(containerColor = Color.Transparent),
                onClick = { onEvent(SongInfoEvent.NavigateUp) })
        })
    }) { innerPadding ->
        Column(modifier = Modifier.padding(top = innerPadding.calculateTopPadding(), start = 16.dp, end = 16.dp)) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f / 1.39f, true)
                    .clip(RoundedCornerShape(32.dp))
            ) {
                PetAIVideoPlayer(exoPlayer = exoPlayer, modifier = Modifier.matchParentSize())
                PetAIIconButton(
                    icon = if (isMuted) R.drawable.ic_music_off else R.drawable.ic_music,
                    colors = PetAIIconButtonDefaults.colors(
                        containerColor = PetAITheme.colors.backgroundPrimary.copy(alpha = 0.5f)
                    ),
                    onClick = {
                        if (isMuted) {
                            isMuted = false
                            exoPlayer.volume = currentVolume
                        } else {
                            isMuted = true
                            currentVolume = exoPlayer.volume
                            exoPlayer.volume = 0f
                        }
                    },
                    modifier = Modifier
                        .padding(16.dp)
                        .size(40.dp)
                        .align(Alignment.TopEnd)
                )
            }
            Spacer(Modifier.size(16.dp))
            PetAITertiaryButton(centerContent = {
                if (uria != null) {
                    val painter = rememberAsyncImagePainter(ImageRequest.Builder(context).data(uria).build())

                    Image(
                        painter = painter,
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .size(48.dp)
                            .clip(RoundedCornerShape(4.dp))
                    )
                } else Icon(imageVector = ImageVector.vectorResource(R.drawable.ic_gallery), contentDescription = null)

                Text(
                    text = stringResource(R.string.song_info_upload_button_text),
                    style = PetAITheme.typography.buttonTextDefault.copy(fontSize = 18.sp)
                )
            }, onClick = { avatarSourceSelectionVisible = true }, modifier = Modifier.fillMaxWidth())
            Spacer(Modifier.size(16.dp))
            PetAIPrimaryButton(
                centerContent = stringResource(R.string.song_info_generate_button_text),
                enabled = uria != null,
                colors = PetAIButtonDefaults.colors(contentColor = PetAITheme.colors.buttonTextPrimary),
                onClick = {
                    if (state.hasSubscription) {
                        state.songInfo?.let {
                            onEvent(SongInfoEvent.GenerateVideo(it.name, uria.toString(), it.url))
                        }
                    } else {
                        isSubscriptionsScreenVisible = true
                    }
                },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(Modifier.size(24.dp))
        }
    }

    if (avatarSourceSelectionVisible) {
        ImageSourceSelectorBottomSheet(
            sheetState = sheetState,
            onDismissRequest = { avatarSourceSelectionVisible = false },
            onCameraSourceClick = { cameraPermissionLauncher.launch(Manifest.permission.CAMERA) },
            onGallerySourceClick = { pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)) })
    }

    if (isSubscriptionsScreenVisible) {
        SubscriptionsContainer(
            modifier = Modifier
                .commonModifier()
                .fillMaxSize(),
            onScreenClose = { isSubscriptionsScreenVisible = false })
    }
}

//TODO Вынести в Utils
fun createImageFile(context: Context): File {
    val fileName = "photo_${System.currentTimeMillis()}"
    val storageDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
    return File.createTempFile(fileName, ".jpg", storageDir)
}