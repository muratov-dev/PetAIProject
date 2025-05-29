package me.yeahapps.mypetai.feature.create.ui.component.create

import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import me.yeahapps.mypetai.R
import me.yeahapps.mypetai.core.ui.component.button.icon.PetAIIconButton
import me.yeahapps.mypetai.core.ui.component.button.icon.PetAIIconButtonDefaults
import me.yeahapps.mypetai.core.ui.theme.PetAITheme

@Composable
fun SelectedAudioCard(
    modifier: Modifier = Modifier,
    audioUri: Uri?,
    isPlaying: Boolean,
    onPlayClick: () -> Unit,
    onPauseClick: () -> Unit,
    onDeleteClick: () -> Unit
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(Color.White.copy(alpha = 0.15f), shape = RoundedCornerShape(20.dp))
            .clip(RoundedCornerShape(20.dp))
            .padding(horizontal = 16.dp, vertical = 20.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        if (audioUri == null) {
            Icon(
                imageVector = ImageVector.vectorResource(R.drawable.ic_play),
                contentDescription = null,
                modifier = Modifier.size(24.dp)
            )
            Text(
                text = stringResource(R.string.create_select_audio_label),
                color = PetAITheme.colors.textPrimary.copy(alpha = 0.5f),
                style = PetAITheme.typography.buttonTextDefault
            )
        } else {
            PetAIIconButton(
                icon = if (isPlaying) R.drawable.ic_pause else R.drawable.ic_play,
                colors = PetAIIconButtonDefaults.colors(containerColor = Color.Transparent),
                onClick = if (isPlaying) onPauseClick else onPlayClick
            )
            DashedDivider(modifier = Modifier.weight(1f))
            PetAIIconButton(
                icon = R.drawable.ic_close_round,
                colors = PetAIIconButtonDefaults.colors(containerColor = Color.Transparent),
                onClick = onDeleteClick
            )
        }
    }
}