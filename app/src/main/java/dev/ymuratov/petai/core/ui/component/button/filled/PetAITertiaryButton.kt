package dev.ymuratov.petai.core.ui.component.button.filled

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun PetAITertiaryButton(
    centerContent: @Composable RowScope.() -> Unit,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    colors: PetAIButtonColors = PetAIButtonDefaults.colors(containerColor = Color.White.copy(alpha = 0.15f)),
    shape: Shape = RoundedCornerShape(20.dp),
    minHeight: Dp = PetAIButtonDefaults.MinHeight,
    paddingValues: PaddingValues = PaddingValues(0.dp)
) {
    Box(
        modifier = modifier.width(IntrinsicSize.Min).background(color = colors.containerColor(enabled), shape = shape)
            .clip(shape).defaultMinSize(minHeight = minHeight).padding(paddingValues),
        contentAlignment = Alignment.Center
    ) {
        Box(modifier = Modifier.matchParentSize().clickable(enabled = enabled, onClick = onClick, role = Role.Button))
        Row(
            modifier = Modifier.matchParentSize().align(Alignment.Center),
            horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            centerContent()
        }
    }
}

