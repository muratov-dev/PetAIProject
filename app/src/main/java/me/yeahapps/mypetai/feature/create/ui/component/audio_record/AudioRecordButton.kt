package me.yeahapps.mypetai.feature.create.ui.component.audio_record

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import me.yeahapps.mypetai.R
import me.yeahapps.mypetai.core.ui.theme.PetAITheme

@Composable
fun AudioRecordButton(
    modifier: Modifier = Modifier, isRecording: Boolean, onStopRecording: () -> Unit, onStartRecording: () -> Unit
) {
    Column(
        modifier = modifier
            .size(184.dp)
            .background(color = PetAITheme.colors.buttonPrimaryDefault, shape = CircleShape)
            .clip(CircleShape)
            .clickable { if (isRecording) onStopRecording else onStartRecording },
        verticalArrangement = Arrangement.spacedBy(10.dp, alignment = Alignment.CenterVertically),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = ImageVector.vectorResource(if (isRecording) R.drawable.ic_stop else R.drawable.ic_record),
            tint = PetAITheme.colors.buttonTextPrimary,
            contentDescription = null,
            modifier = Modifier.size(24.dp)
        )
        Text(
            text = if (isRecording) stringResource(R.string.audio_record_recording_button_text)
            else stringResource(R.string.audio_record_not_recording_button_text),
            color = PetAITheme.colors.buttonTextPrimary,
            style = PetAITheme.typography.buttonTextDefault
        )
    }
}