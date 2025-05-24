package me.yeahapps.mypetai.feature.create.ui.screen

import android.Manifest
import android.content.Context
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.contract.ActivityResultContracts.PickVisualMedia.ImageOnly
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.FileProvider
import androidx.core.net.toUri
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.serialization.Serializable
import me.yeahapps.mypetai.R
import me.yeahapps.mypetai.core.ui.component.button.filled.PetAIButtonDefaults
import me.yeahapps.mypetai.core.ui.component.button.filled.PetAISecondaryButton
import me.yeahapps.mypetai.core.ui.component.button.icon.PetAIIconButton
import me.yeahapps.mypetai.core.ui.component.button.icon.PetAIIconButtonDefaults
import me.yeahapps.mypetai.core.ui.component.topbar.PetAISecondaryTopAppBar
import me.yeahapps.mypetai.core.ui.theme.PetAITheme
import me.yeahapps.mypetai.core.ui.utils.collectFlowWithLifecycle
import me.yeahapps.mypetai.feature.create.ui.AudioTranscoder
import me.yeahapps.mypetai.feature.create.ui.action.CreateAction
import me.yeahapps.mypetai.feature.create.ui.event.CreateEvent
import me.yeahapps.mypetai.feature.create.ui.state.CreateState
import me.yeahapps.mypetai.feature.create.ui.viewmodel.CreateViewModel
import me.yeahapps.mypetai.feature.discover.ui.screen.createImageFile
import timber.log.Timber
import java.io.File
import java.io.FileOutputStream

@Serializable
object CreateScreen

@Composable
fun CreateContainer(
    modifier: Modifier = Modifier, viewModel: CreateViewModel = hiltViewModel(), navigateToRecord: () -> Unit
) {
    val context = LocalContext.current
    val exoPlayer = remember {
        ExoPlayer.Builder(context).build().apply {
            playWhenReady = false
            prepare()
        }
    }

    val state by viewModel.viewState.collectAsStateWithLifecycle()
    state.userAudioUri?.let { exoPlayer.setMediaItem(MediaItem.fromUri(it)) }
    viewModel.viewActions.collectFlowWithLifecycle(viewModel) { action ->
        when (action) {
            CreateAction.PauseAudio -> exoPlayer.pause()
            CreateAction.PlayAudio -> exoPlayer.play()
            CreateAction.RecordAudio -> navigateToRecord()
            null -> {}
        }
    }

    val lifecycleOwner = LocalLifecycleOwner.current
    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_CREATE -> viewModel.obtainEvent(CreateEvent.CheckRecordedAudio)
                else -> {}
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    DisposableEffect(Unit) {
        onDispose {
            exoPlayer.release()
        }
    }

    CreateContent(
        context = context,
        modifier = modifier.systemBarsPadding(),
        state = state,
        onEvent = remember { { event -> viewModel.obtainEvent(event) } },
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CreateContent(
    context: Context,
    modifier: Modifier = Modifier,
    state: CreateState = CreateState(),
    onEvent: (CreateEvent) -> Unit = {},
) {
    var isButtonExpanded by remember { mutableStateOf(true) }
    var isFileSelectEnabled by remember { mutableStateOf(true) }
    val scope = rememberCoroutineScope()
    val sheetState = rememberModalBottomSheetState()
    var avatarSourceSelectionVisible by remember { mutableStateOf(false) }

    val pickMedia = rememberLauncherForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
        if (uri != null) {
            avatarSourceSelectionVisible = false
            onEvent(CreateEvent.OnUserImageSelect(uri))
        } else Timber.d("No media selected")
    }

    val photoUri: Uri = androidx.core.content.FileProvider.getUriForFile(
        context, "${context.packageName}.provider", createImageFile(context)
    )
    val cameraLauncher = rememberLauncherForActivityResult(ActivityResultContracts.TakePicture()) { success ->
        if (success) {
            avatarSourceSelectionVisible = false
            onEvent(CreateEvent.OnUserImageSelect(photoUri))
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


    val pickAudioLauncher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        uri?.let {
            val inputFile = copyToCache(context, it)
            val outputFile = File(context.cacheDir, "${inputFile.name}_converted.m4a")

            CoroutineScope(Dispatchers.IO).launch {
                isFileSelectEnabled = false
                withContext(Dispatchers.Main) {
                    Toast.makeText(context, "Your sound is converting, please wait.", Toast.LENGTH_SHORT).show()
                }
                val transcoder = AudioTranscoder(
                    inputPath = inputFile.absolutePath, outputPath = outputFile.absolutePath, maxDurationSec = 30
                )
                val success = transcoder.transcode()
                onEvent(CreateEvent.OnAudioSelect(outputFile.toUri()))
                Timber.d("Result: $success. File: ${outputFile.absolutePath}")
                isFileSelectEnabled = true
            }
        }

    }

    LaunchedEffect(isButtonExpanded) {
        if (isButtonExpanded) {
            delay(5000)
            isButtonExpanded = false
        }
    }

    Column(modifier = modifier.padding(horizontal = 16.dp)) {
        PetAISecondaryTopAppBar(title = { Text(text = "Create") }, endAction = {
            Button(
                onClick = {
                    if (isButtonExpanded) {
                    } else {
                        isButtonExpanded = true
                    }
                },
                shape = RoundedCornerShape(100.dp),
                contentPadding = PaddingValues(vertical = 8.dp, horizontal = 16.dp),
                colors = ButtonColors(
                    containerColor = PetAITheme.colors.buttonPrimaryDefault,
                    contentColor = PetAITheme.colors.buttonTextPrimary,
                    disabledContainerColor = Color.White,
                    disabledContentColor = Color.Black
                ),
                modifier = Modifier.wrapContentSize()
            ) {
                Row(
                    modifier = Modifier.animateContentSize(tween(300)),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                ) {
                    Icon(imageVector = ImageVector.vectorResource(R.drawable.ic_ribbon), contentDescription = null)
                    if (isButtonExpanded) {
                        Text(
                            text = "Get PRO",
                            color = PetAITheme.colors.buttonTextPrimary,
                            style = PetAITheme.typography.buttonTextDefault
                        )
                    }
                }
            }
        })
        Spacer(Modifier.size(20.dp))
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(164.dp)
                .background(Color.White.copy(alpha = 0.15f), shape = RoundedCornerShape(20.dp))
                .clip(RoundedCornerShape(20.dp))
                .clickable(onClick = { avatarSourceSelectionVisible = true }),
            contentAlignment = Alignment.Center
        ) {
            if (state.userImageUri != null) {
                val painter = rememberAsyncImagePainter(
                    ImageRequest.Builder(context).data(state.userImageUri).build()
                )
                Image(
                    painter = painter,
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.matchParentSize()
                )
            } else {
                Row(
                    verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        imageVector = ImageVector.vectorResource(R.drawable.ic_gallery),
                        contentDescription = null,
                        modifier = Modifier.size(24.dp)
                    )
                    Text(
                        text = "Upload Image",
                        color = PetAITheme.colors.textPrimary,
                        style = PetAITheme.typography.headlineSemiBold.copy(fontSize = 18.sp, lineHeight = 27.sp),
                    )
                }
            }
        }

        val label = buildAnnotatedString {
            withStyle(PetAITheme.typography.buttonTextDefault.toSpanStyle()) {
                append("Add Audio ")
            }
            withStyle(PetAITheme.typography.buttonTextRegular.toSpanStyle()) {
                append("(Maximum 30 seconds)")
            }
        }
        Spacer(Modifier.size(20.dp))
        Text(text = label, color = PetAITheme.colors.textPrimary, modifier = Modifier.fillMaxWidth())
        Spacer(Modifier.size(24.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White.copy(alpha = 0.15f), shape = RoundedCornerShape(20.dp))
                .clip(RoundedCornerShape(20.dp))
                .padding(horizontal = 16.dp, vertical = 20.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            if (state.userAudioUri == null) {
                Icon(
                    imageVector = ImageVector.vectorResource(R.drawable.ic_play),
                    contentDescription = null,
                    modifier = Modifier.size(24.dp)
                )
                Text(
                    text = "Please use the bottom tool to add audio",
                    color = PetAITheme.colors.textPrimary.copy(alpha = 0.5f),
                    style = PetAITheme.typography.buttonTextDefault
                )
            } else {
                PetAIIconButton(
                    icon = R.drawable.ic_play,
                    colors = PetAIIconButtonDefaults.colors(containerColor = Color.Transparent),
                    onClick = { onEvent(CreateEvent.PlayAudio) })
                DashedDivider(modifier = Modifier.weight(1f))
                PetAIIconButton(
                    icon = R.drawable.ic_close_round,
                    colors = PetAIIconButtonDefaults.colors(containerColor = Color.Transparent),
                    onClick = { onEvent(CreateEvent.DeleteAudio) })
            }
        }
        Spacer(Modifier.size(10.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Column(
                modifier = Modifier
                    .background(Color.White.copy(alpha = 0.15f), shape = RoundedCornerShape(20.dp))
                    .clip(RoundedCornerShape(20.dp))
                    .weight(1f)
                    .clickable(enabled = isFileSelectEnabled) { pickAudioLauncher.launch("audio/*") }
                    .padding(vertical = 20.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalAlignment = Alignment.CenterHorizontally) {
                Icon(
                    imageVector = ImageVector.vectorResource(R.drawable.ic_upload),
                    contentDescription = null,
                    modifier = Modifier.size(24.dp)
                )
                Text(
                    text = "Upload",
                    color = PetAITheme.colors.textPrimary,
                    style = PetAITheme.typography.buttonTextDefault
                )
            }

            Column(
                modifier = Modifier
                    .background(Color.White.copy(alpha = 0.15f), shape = RoundedCornerShape(20.dp))
                    .clip(RoundedCornerShape(20.dp))
                    .weight(1f)
                    .clickable { onEvent(CreateEvent.RecordAudio) }
                    .padding(vertical = 20.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalAlignment = Alignment.CenterHorizontally) {
                Icon(
                    imageVector = ImageVector.vectorResource(R.drawable.ic_record),
                    contentDescription = null,
                    modifier = Modifier.size(24.dp)
                )
                Text(
                    text = "Record",
                    color = PetAITheme.colors.textPrimary,
                    style = PetAITheme.typography.buttonTextDefault
                )
            }
        }
    }

    if (avatarSourceSelectionVisible) {
        ModalBottomSheet(
            onDismissRequest = { avatarSourceSelectionVisible = false }, sheetState = sheetState,
            dragHandle = {
                Canvas(
                    Modifier
                        .padding(top = 8.dp, bottom = 8.dp)
                        .width(36.dp)
                        .height(5.dp)
                        .clip(RoundedCornerShape(100.dp))
                ) {
                    drawRect(color = Color(0x667F7F7F))
                    drawRect(color = Color(0x80C2C2C2), blendMode = BlendMode.Multiply)
                }
            },
            containerColor = Color(0xFF221F03),
        ) {
            Column {
                Row(
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .fillMaxWidth()
                ) {
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
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .fillMaxWidth()
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
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .fillMaxWidth()
                )
                Spacer(Modifier.size(32.dp))
            }
        }
    }
}

@Composable
fun DashedDivider(
    color: Color = PetAITheme.colors.buttonPrimaryDefault,
    strokeWidth: Dp = 1.dp,
    dashLength: Dp = 5.dp,
    gapLength: Dp = 5.dp,
    modifier: Modifier = Modifier.fillMaxWidth()
) {
    val density = LocalDensity.current
    Canvas(modifier = modifier.height(strokeWidth)) {
        val y = size.height / 2
        val dashPx = with(density) { dashLength.toPx() }
        val gapPx = with(density) { gapLength.toPx() }

        var startX = 0f
        while (startX < size.width) {
            val endX = (startX + dashPx).coerceAtMost(size.width)
            drawLine(
                color = color,
                start = Offset(x = startX, y = y),
                end = Offset(x = endX, y = y),
                strokeWidth = strokeWidth.toPx()
            )
            startX += dashPx + gapPx
        }
    }
}

fun copyToCache(context: Context, uri: Uri): File {
    val inputStream = context.contentResolver.openInputStream(uri)!!
    val file = File(context.cacheDir, "input_audio")
    inputStream.use { input ->
        FileOutputStream(file).use { output ->
            input.copyTo(output)
        }
    }
    return file
}
