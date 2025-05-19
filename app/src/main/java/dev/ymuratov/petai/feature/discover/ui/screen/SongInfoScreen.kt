package dev.ymuratov.petai.feature.discover.ui.screen

import android.Manifest
import android.content.Context
import android.net.Uri
import android.os.Environment
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.contract.ActivityResultContracts.PickVisualMedia.ImageOnly
import androidx.annotation.OptIn
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.FileProvider
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
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import dev.ymuratov.petai.R
import dev.ymuratov.petai.core.ui.component.button.filled.PetAIButtonDefaults
import dev.ymuratov.petai.core.ui.component.button.filled.PetAIPrimaryButton
import dev.ymuratov.petai.core.ui.component.button.filled.PetAISecondaryButton
import dev.ymuratov.petai.core.ui.component.button.filled.PetAITertiaryButton
import dev.ymuratov.petai.core.ui.component.button.icon.PetAIIconButton
import dev.ymuratov.petai.core.ui.component.button.icon.PetAIIconButtonDefaults
import dev.ymuratov.petai.core.ui.component.topbar.PetAITopAppBar
import dev.ymuratov.petai.core.ui.component.topbar.PetAITopBarTitleText
import dev.ymuratov.petai.core.ui.theme.PetAITheme
import dev.ymuratov.petai.feature.discover.domain.model.SongModel
import dev.ymuratov.petai.feature.discover.ui.event.SongInfoEvent
import dev.ymuratov.petai.feature.discover.ui.state.SongInfoState
import dev.ymuratov.petai.feature.discover.ui.viewmodel.SongInfoViewModel
import kotlinx.serialization.Serializable
import timber.log.Timber
import java.io.File

@Serializable
data class SongInfoScreen(val song: SongModel)

@Composable
fun SongInfoContainer(
    modifier: Modifier = Modifier, viewModel: SongInfoViewModel = hiltViewModel(), navigateUp: () -> Unit = {}
) {
    val state by viewModel.viewState.collectAsStateWithLifecycle()

    val context = LocalContext.current
    val exoPlayer = remember {
        ExoPlayer.Builder(context).build().apply {
            state.songInfo?.let {
                setMediaItem(MediaItem.fromUri(it.videos.first().video))
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

    SongInfoContent(
        modifier = modifier.systemBarsPadding(),
        exoPlayer = exoPlayer,
        state = state,
        onEvent = remember { { event -> viewModel.obtainEvent(event) } },
        navigateUp = navigateUp
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
    navigateUp: () -> Unit
) {
    val context = LocalContext.current
    val sheetState = rememberModalBottomSheetState()
    var uria by remember { mutableStateOf<Uri?>(null) }
    var isMuted by remember { mutableStateOf(false) }
    var currentVolume by remember { mutableFloatStateOf(0f) }
    var avatarSourceSelectionVisible by remember { mutableStateOf(false) }

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
            if (isGranted) {
                cameraLauncher.launch(photoUri)
            } else {
                // Пользователь отказал
            }
        })


    Scaffold(modifier = modifier, topBar = {
        PetAITopAppBar(title = {
            state.songInfo?.let { PetAITopBarTitleText(text = it.name) }
        }, navigationIcon = {
            PetAIIconButton(
                icon = R.drawable.ic_arrow_left,
                colors = PetAIIconButtonDefaults.colors(containerColor = Color.Transparent),
                onClick = navigateUp
            )
        })
    }) { innerPadding ->
        Column(modifier = Modifier.padding(top = innerPadding.calculateTopPadding(), start = 16.dp, end = 16.dp)) {
            Box(
                modifier = Modifier.fillMaxWidth().aspectRatio(1f / 1.39f, true).clip(RoundedCornerShape(32.dp))
            ) {
                AndroidView(modifier = Modifier.matchParentSize(), factory = { context ->
                    PlayerView(context).apply {
                        player = exoPlayer
                        useController = false
                        resizeMode = AspectRatioFrameLayout.RESIZE_MODE_FILL
                    }
                })
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
                    modifier = Modifier.padding(16.dp).size(40.dp).align(Alignment.TopEnd)
                )
            }
            Spacer(Modifier.size(16.dp))
            PetAITertiaryButton(centerContent = {
                if (uria != null) {
                    val painter = rememberAsyncImagePainter(
                        ImageRequest.Builder(context).data(uria).build()
                    )

                    Image(
                        painter = painter,
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.size(48.dp).clip(RoundedCornerShape(4.dp))
                    )
                } else {
                    Icon(
                        imageVector = ImageVector.vectorResource(R.drawable.ic_gallery), contentDescription = null
                    )
                }
                Text(text = "Upload Image", style = PetAITheme.typography.buttonTextDefault.copy(fontSize = 18.sp))
            }, onClick = { avatarSourceSelectionVisible = true }, modifier = Modifier.fillMaxWidth())
            Spacer(Modifier.size(16.dp))
            PetAIPrimaryButton(
                centerContent = "Generate",
                enabled = uria != null,
                colors = PetAIButtonDefaults.colors(contentColor = PetAITheme.colors.buttonTextPrimary),
                onClick = {},
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(Modifier.size(24.dp))
        }
    }

    if (avatarSourceSelectionVisible) {
        ModalBottomSheet(
            onDismissRequest = { avatarSourceSelectionVisible = false }, sheetState = sheetState,
            dragHandle = {
                Canvas(
                    Modifier.padding(top = 8.dp, bottom = 8.dp).width(36.dp).height(5.dp)
                        .clip(RoundedCornerShape(100.dp))
                ) {
                    drawRect(color = Color(0x667F7F7F))
                    drawRect(color = Color(0x80C2C2C2), blendMode = BlendMode.Multiply)
                }
            },
            containerColor = Color(0xFF221F03),
        ) {
            Column {
                Row(modifier = Modifier.padding(horizontal = 16.dp).fillMaxWidth()) {
                    Text(
                        text = "Upload from",
                        style = PetAITheme.typography.buttonTextDefault,
                        color = PetAITheme.colors.buttonTextSecondary,
                        modifier = Modifier.weight(1f)
                    )
                    PetAIIconButton(
                        icon = R.drawable.ic_close,
                        colors = PetAIIconButtonDefaults.colors(containerColor = Color.Transparent),
                        onClick = { avatarSourceSelectionVisible = false })
                }
                Spacer(Modifier.size(24.dp))
                PetAISecondaryButton(
                    text = "Gallery",
                    colors = PetAIButtonDefaults.colors(
                        containerColor = PetAITheme.colors.buttonSecondaryDefault,
                        contentColor = PetAITheme.colors.buttonTextSecondary,
                    ),
                    startContent = {
                        Icon(imageVector = ImageVector.vectorResource(R.drawable.ic_gallery), contentDescription = null)
                    },
                    onClick = { pickMedia.launch(PickVisualMediaRequest(ImageOnly)) },
                    modifier = Modifier.padding(horizontal = 16.dp).fillMaxWidth()
                )
                Spacer(Modifier.size(16.dp))
                PetAISecondaryButton(
                    text = "Camera",
                    startContent = {
                        Icon(imageVector = ImageVector.vectorResource(R.drawable.ic_camera), contentDescription = null)
                    },
                    colors = PetAIButtonDefaults.colors(
                        containerColor = PetAITheme.colors.buttonSecondaryDefault,
                        contentColor = PetAITheme.colors.buttonTextSecondary,
                    ),
                    onClick = { cameraPermissionLauncher.launch(Manifest.permission.CAMERA) },
                    modifier = Modifier.padding(horizontal = 16.dp).fillMaxWidth()
                )
                Spacer(Modifier.size(32.dp))
            }
        }
    }
}

fun createImageFile(context: Context): File {
    val fileName = "photo_${System.currentTimeMillis()}"
    val storageDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
    return File.createTempFile(fileName, ".jpg", storageDir)
}