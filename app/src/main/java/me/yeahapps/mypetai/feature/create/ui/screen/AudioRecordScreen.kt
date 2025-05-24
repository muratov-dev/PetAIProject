package me.yeahapps.mypetai.feature.create.ui.screen

import android.Manifest
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
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
import me.yeahapps.mypetai.feature.create.ui.action.AudioRecordAction
import me.yeahapps.mypetai.feature.create.ui.event.AudioRecordEvent
import me.yeahapps.mypetai.feature.create.ui.state.AudioRecordState
import me.yeahapps.mypetai.feature.create.ui.viewmodel.AudioRecordViewModel
import java.util.Locale
import java.util.concurrent.TimeUnit

@Serializable
object AudioRecordScreen

@Composable
fun AudioRecordContainer(
    modifier: Modifier = Modifier, viewModel: AudioRecordViewModel = hiltViewModel(), navigateUp: () -> Unit
) {
    val state by viewModel.viewState.collectAsStateWithLifecycle()

    viewModel.viewActions.collectFlowWithLifecycle(viewModel) { action ->
        when (action) {
            AudioRecordAction.NavigateUp -> navigateUp()
            null -> {}
        }
    }

    val micPermission =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.RequestPermission()) { granted ->
            if (!granted) navigateUp()
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
        PetAITopAppBar(title = { PetAITopBarTitleText(text = "Record") }, navigationIcon = {
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
                text = if (state.isRecording) "Click the button below to stop recording"
                else "Click the button below to start recording",
                color = PetAITheme.colors.textPrimary.copy(alpha = 0.7f),
                style = PetAITheme.typography.textMedium.copy(fontSize = 16.sp, lineHeight = 24.sp),
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .padding(horizontal = 16.dp, vertical = 24.dp)
                    .fillMaxWidth()
                    .align(Alignment.TopCenter)
            )

            Column(
                modifier = Modifier
                    .size(184.dp)
                    .background(color = PetAITheme.colors.buttonPrimaryDefault, shape = CircleShape)
                    .clip(CircleShape)
                    .align(Alignment.Center)
                    .clickable {
                        if (state.isRecording) onEvent(AudioRecordEvent.StopRecording)
                        else onEvent(AudioRecordEvent.StartRecording)
                    },
                verticalArrangement = Arrangement.spacedBy(10.dp, alignment = Alignment.CenterVertically),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    imageVector = ImageVector.vectorResource(if (state.isRecording) R.drawable.ic_stop else R.drawable.ic_record),
                    tint = PetAITheme.colors.buttonTextPrimary,
                    contentDescription = null,
                    modifier = Modifier.size(24.dp)
                )
                Text(
                    text = if (state.isRecording) "Stop Record" else "Start Record",
                    color = PetAITheme.colors.buttonTextPrimary,
                    style = PetAITheme.typography.buttonTextDefault
                )
            }

            Text(
                text = formatMillisecondsToMmSs(state.audioDuration),
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

private fun formatMillisecondsToMmSs(milliseconds: Long): String {
    val minutes = TimeUnit.MILLISECONDS.toMinutes(milliseconds)
    val seconds = TimeUnit.MILLISECONDS.toSeconds(milliseconds) - TimeUnit.MINUTES.toSeconds(minutes)

    return String.format(Locale("ru", "RU"), "%02d:%02d", minutes, seconds)
}