package me.yeahapps.mypetai.feature.create.ui.screen

import android.Manifest
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.serialization.Serializable
import me.yeahapps.mypetai.R
import me.yeahapps.mypetai.core.ui.component.button.icon.PetAIIconButton
import me.yeahapps.mypetai.core.ui.component.button.icon.PetAIIconButtonDefaults
import me.yeahapps.mypetai.core.ui.component.topbar.PetAITopAppBar
import me.yeahapps.mypetai.core.ui.component.topbar.PetAITopBarTitleText
import me.yeahapps.mypetai.core.ui.theme.PetAITheme
import me.yeahapps.mypetai.core.ui.utils.collectFlowWithLifecycle
import me.yeahapps.mypetai.core.ui.utils.formatMillisecondsToMmSs
import me.yeahapps.mypetai.feature.create.ui.action.AudioRecordAction
import me.yeahapps.mypetai.feature.create.ui.component.audio_record.AudioRecordButton
import me.yeahapps.mypetai.feature.create.ui.event.AudioRecordEvent
import me.yeahapps.mypetai.feature.create.ui.state.AudioRecordState
import me.yeahapps.mypetai.feature.create.ui.viewmodel.AudioRecordViewModel

@Serializable
object AudioRecordScreen

@Composable
fun AudioRecordContainer(
    modifier: Modifier = Modifier, viewModel: AudioRecordViewModel = hiltViewModel(), navigateUp: () -> Unit
) {
    val context = LocalContext.current
    val state by viewModel.viewState.collectAsStateWithLifecycle()

    viewModel.viewActions.collectFlowWithLifecycle(viewModel) { action ->
        when (action) {
            AudioRecordAction.NavigateUp -> navigateUp()
            null -> {}
        }
    }

    val micPermission =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.RequestPermission()) { granted ->
            if (!granted) {
                Toast.makeText(context, context.getString(R.string.audio_record_permission_denied), Toast.LENGTH_SHORT)
                    .show()
                navigateUp()
            } else {
                viewModel.obtainEvent(AudioRecordEvent.StartRecording)
            }
        }

    LaunchedEffect(Unit) {
        micPermission.launch(Manifest.permission.RECORD_AUDIO)
    }

    AudioRecordContent(
        modifier = modifier.systemBarsPadding(),
        state = state,
        onEvent = remember { { event -> viewModel.obtainEvent(event) } })
}

@Composable
private fun AudioRecordContent(
    modifier: Modifier = Modifier,
    state: AudioRecordState = AudioRecordState(),
    onEvent: (AudioRecordEvent) -> Unit = {}
) {
    Scaffold(modifier = modifier, topBar = {
        PetAITopAppBar(
            title = { PetAITopBarTitleText(text = stringResource(R.string.audio_record_title)) },
            navigationIcon = {
                PetAIIconButton(
                    icon = R.drawable.ic_arrow_left,
                    onClick = { onEvent(AudioRecordEvent.NavigateUp) },
                    colors = PetAIIconButtonDefaults.colors(containerColor = Color.Transparent)
                )
            })
    }) { innerPadding ->
        Box(
            modifier = Modifier
                .padding(top = innerPadding.calculateTopPadding())
                .fillMaxSize()
        ) {
            Text(
                text = if (state.isRecording) stringResource(R.string.audio_record_recording_title)
                else stringResource(R.string.audio_record_not_recording_title),
                color = PetAITheme.colors.textPrimary.copy(alpha = 0.7f),
                style = PetAITheme.typography.textMedium.copy(fontSize = 16.sp, lineHeight = 24.sp),
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .padding(horizontal = 16.dp, vertical = 24.dp)
                    .fillMaxWidth()
                    .align(Alignment.TopCenter)
            )

            AudioRecordButton(
                modifier = Modifier.align(Alignment.Center), isRecording = state.isRecording,
                onStopRecording = { onEvent(AudioRecordEvent.StopRecording) },
                onStartRecording = { onEvent(AudioRecordEvent.StartRecording) },
            )

            Text(
                text = state.audioDuration.formatMillisecondsToMmSs(),
                color = PetAITheme.colors.textPrimary.copy(alpha = 0.7f),
                style = PetAITheme.typography.textMedium.copy(fontSize = 16.sp, lineHeight = 24.sp),
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .padding(start = 16.dp, end = 16.dp, bottom = 80.dp)
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter)
            )
        }
    }
}