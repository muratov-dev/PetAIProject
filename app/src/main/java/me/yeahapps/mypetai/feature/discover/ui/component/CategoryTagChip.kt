package me.yeahapps.mypetai.feature.discover.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import me.yeahapps.mypetai.core.ui.theme.PetAITheme


@Composable
fun CategoryTagChip(modifier: Modifier = Modifier, label: String, selected: Boolean, onChipClick: () -> Unit) {
    Box(
        modifier = modifier.widthIn(min = 64.dp).wrapContentHeight().background(
            color = if (selected) PetAITheme.colors.buttonPrimaryDefault else Color.White.copy(alpha = 0.15f),
            shape = RoundedCornerShape(100.dp)
        ).clip(RoundedCornerShape(100.dp)).clickable(onClick = onChipClick).padding(12.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = label,
            style = PetAITheme.typography.textMedium,
            color = if (selected) PetAITheme.colors.buttonTextPrimary else PetAITheme.colors.buttonTextSecondary
        )
    }
}