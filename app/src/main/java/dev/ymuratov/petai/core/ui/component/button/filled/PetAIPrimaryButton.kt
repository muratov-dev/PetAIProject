package dev.ymuratov.petai.core.ui.component.button.filled

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import dev.ymuratov.petai.core.ui.component.button.PetAIButtonText

@Composable
fun PetAIPrimaryButton(
    centerContent: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    colors: PetAIButtonColors = PetAIButtonDefaults.colors(),
    shape: Shape = PetAIButtonDefaults.Shape,
    minHeight: Dp = PetAIButtonDefaults.MinHeight,
    paddingValues: PaddingValues = PaddingValues(0.dp)
) {
    Box(
        modifier = modifier.width(IntrinsicSize.Min).background(color = colors.containerColor(enabled), shape = shape)
            .clip(shape).defaultMinSize(minHeight = minHeight).padding(paddingValues),
        contentAlignment = Alignment.Center
    ) {
        Box(modifier = Modifier.matchParentSize().clickable(enabled = enabled, onClick = onClick, role = Role.Button))
        PetAIButtonText(text = centerContent)
    }
}

