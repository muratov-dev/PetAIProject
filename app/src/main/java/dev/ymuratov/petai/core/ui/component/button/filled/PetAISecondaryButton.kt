package dev.ymuratov.petai.core.ui.component.button.filled

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.LocalContentColor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import dev.ymuratov.petai.core.ui.component.button.PetAIButtonText
import dev.ymuratov.petai.core.ui.theme.PetAITheme

@Composable
fun PetAISecondaryButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    startContent: @Composable() (() -> Unit)? = null,
    endContent: @Composable() (() -> Unit)? = null,
    enabled: Boolean = true,
    colors: PetAIButtonColors = PetAIButtonDefaults.colors(
        containerColor = PetAITheme.colors.buttonSecondaryDefault,
        contentColor = PetAITheme.colors.buttonTextSecondary,
    ),
    shape: Shape = RoundedCornerShape(14.dp),
    minHeight: Dp = 52.dp,
    paddingValues: PaddingValues = PaddingValues(horizontal = 10.dp, vertical = 16.dp)
) {
    Box(
        modifier = modifier.width(IntrinsicSize.Min).background(color = colors.containerColor(enabled), shape = shape)
            .clip(shape).defaultMinSize(minHeight = minHeight)
            .clickable(enabled = enabled, onClick = onClick, role = Role.Button).padding(paddingValues),
        contentAlignment = Alignment.Center
    ) {
        Box(modifier = Modifier.matchParentSize())
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalAlignment = Alignment.CenterVertically) {
            CompositionLocalProvider(LocalContentColor provides colors.contentColor(enabled)){
                startContent?.let {
                    startContent()
                }
                PetAIButtonText(
                    text = text,
                    style = PetAITheme.typography.buttonTextRegular,
                    textAlign = TextAlign.Start,
                    modifier = Modifier.weight(1f)
                )
                endContent?.let {
                    endContent()
                }
            }
        }
    }
}

