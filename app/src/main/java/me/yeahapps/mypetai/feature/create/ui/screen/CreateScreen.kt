package me.yeahapps.mypetai.feature.create.ui.screen

import android.Manifest
import android.content.Context
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.contract.ActivityResultContracts.PickVisualMedia.ImageOnly
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.core.content.FileProvider
import androidx.core.net.toUri
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import me.yeahapps.mypetai.R
import me.yeahapps.mypetai.core.ui.component.bottomsheet.ImageSourceSelectorBottomSheet
import me.yeahapps.mypetai.core.ui.component.button.GetProButton
import me.yeahapps.mypetai.core.ui.component.button.filled.PetAIPrimaryButton
import me.yeahapps.mypetai.core.ui.component.topbar.PetAISecondaryTopAppBar
import me.yeahapps.mypetai.core.ui.theme.PetAITheme
import me.yeahapps.mypetai.core.ui.utils.collectFlowWithLifecycle
import me.yeahapps.mypetai.core.ui.utils.copyToCache
import me.yeahapps.mypetai.feature.create.ui.AudioTranscoder
import me.yeahapps.mypetai.feature.create.ui.action.CreateAction
import me.yeahapps.mypetai.feature.create.ui.component.create.ImageSelectCard
import me.yeahapps.mypetai.feature.create.ui.component.create.SelectedAudioCard
import me.yeahapps.mypetai.feature.create.ui.event.CreateEvent
import me.yeahapps.mypetai.feature.create.ui.state.CreateState
import me.yeahapps.mypetai.feature.create.ui.viewmodel.CreateViewModel
import me.yeahapps.mypetai.feature.discover.ui.screen.createImageFile
import timber.log.Timber
import java.io.File

@Composable
fun CreateContainer(
    modifier: Modifier = Modifier,
    viewModel: CreateViewModel = hiltViewModel(),
    navigateToProcessing: (String, String) -> Unit,
    navigateToSubscriptions: () -> Unit,
    navigateToRecord: () -> Unit
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
            is CreateAction.StartCreatingVideo -> navigateToProcessing(action.imageUri, action.audioUri)
            CreateAction.NavigateToSubscriptions -> navigateToSubscriptions()
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
    var isConvertingAudio by remember { mutableStateOf(false) }
    var imageSourceSelectorVisible by remember { mutableStateOf(false) }

    val sheetState = rememberModalBottomSheetState()

    val pickMedia = rememberLauncherForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
        if (uri != null) {
            imageSourceSelectorVisible = false
            onEvent(CreateEvent.OnUserImageSelect(uri))
        } else Timber.d("No media selected")
    }

    var isButtonExpanded by remember { mutableStateOf(true) }
    LaunchedEffect(isButtonExpanded) {
        if (isButtonExpanded) {
            delay(5000)
            isButtonExpanded = false
        }
    }

    val photoUri: Uri = FileProvider.getUriForFile(
        context, "${context.packageName}.provider", createImageFile(context)
    )
    val cameraLauncher = rememberLauncherForActivityResult(ActivityResultContracts.TakePicture()) { success ->
        if (success) {
            imageSourceSelectorVisible = false
            onEvent(CreateEvent.OnUserImageSelect(photoUri))
        } else Timber.d("No media selected")
    }

    val cameraPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { isGranted -> if (isGranted) cameraLauncher.launch(photoUri) })

    val pickAudioLauncher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        uri?.let {
            val inputFile = copyToCache(context, it)
            val outputFile = File(context.cacheDir, "${inputFile.name}_converted.m4a")

            CoroutineScope(Dispatchers.IO).launch {
                isConvertingAudio = true
                withContext(Dispatchers.Main) {
                    Toast.makeText(context, "Your sound is converting, please wait.", Toast.LENGTH_SHORT).show()
                }
                val transcoder = AudioTranscoder(
                    inputPath = inputFile.absolutePath, outputPath = outputFile.absolutePath, maxDurationSec = 30
                )
                val success = transcoder.transcode()
                onEvent(CreateEvent.OnAudioSelect(outputFile.toUri()))
                Timber.d("Result: $success. File: ${outputFile.absolutePath}")
                isConvertingAudio = false
            }
        }

    }

    Column(modifier = modifier.padding(horizontal = 16.dp)) {
        PetAISecondaryTopAppBar(title = { Text(text = stringResource(R.string.create_label)) }, endAction = {
            GetProButton(
                modifier = Modifier
                    .wrapContentSize()
                    .systemBarsPadding()
            ) {
                if (isButtonExpanded) onEvent(CreateEvent.NavigateToSubscriptions)
                else isButtonExpanded = true
            }
        })
        Spacer(Modifier.size(20.dp))
        ImageSelectCard(context = context, imageUri = state.userImageUri) { imageSourceSelectorVisible = true }

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
        SelectedAudioCard(
            audioUri = state.userAudioUri,
            isPlaying = state.isAudioPlaying,
            onPlayClick = { onEvent(CreateEvent.PlayAudio) },
            onPauseClick = { onEvent(CreateEvent.PauseAudio) },
            onDeleteClick = { onEvent(CreateEvent.DeleteAudio) },
        )
        Spacer(Modifier.size(10.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Column(
                modifier = Modifier
                    .defaultMinSize(minHeight = 88.dp)
                    .background(Color.White.copy(alpha = 0.15f), shape = RoundedCornerShape(20.dp))
                    .clip(RoundedCornerShape(20.dp))
                    .weight(1f)
                    .clickable(enabled = !isConvertingAudio) { pickAudioLauncher.launch("audio/*") }
                    .padding(vertical = 20.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterVertically),
                horizontalAlignment = Alignment.CenterHorizontally) {
                if (!isConvertingAudio) {
                    Icon(
                        imageVector = ImageVector.vectorResource(R.drawable.ic_upload),
                        contentDescription = null,
                        modifier = Modifier.size(24.dp)
                    )
                }
                Text(
                    text = if (!isConvertingAudio) stringResource(R.string.create_upload_audio_label)
                    else stringResource(R.string.create_upload_audio_converting_label),
                    color = PetAITheme.colors.textPrimary,
                    style = PetAITheme.typography.buttonTextDefault
                )
            }

            Column(
                modifier = Modifier
                    .defaultMinSize(minHeight = 88.dp)
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
                    text = stringResource(R.string.create_record_audio_label),
                    color = PetAITheme.colors.textPrimary,
                    style = PetAITheme.typography.buttonTextDefault
                )
            }
        }
        Spacer(Modifier.size(60.dp))
        PetAIPrimaryButton(
            centerContent = stringResource(R.string.create_label),
            enabled = state.isButtonEnabled && state.hasSubscription,
            onClick = { onEvent(CreateEvent.StartCreatingVideo) },
            modifier = Modifier.fillMaxWidth()
        )
    }


    if (imageSourceSelectorVisible) {
        ImageSourceSelectorBottomSheet(
            sheetState = sheetState,
            onDismissRequest = { imageSourceSelectorVisible = false },
            onCameraSourceClick = { cameraPermissionLauncher.launch(Manifest.permission.CAMERA) },
            onGallerySourceClick = { pickMedia.launch(PickVisualMediaRequest(mediaType = ImageOnly)) })
    }
}