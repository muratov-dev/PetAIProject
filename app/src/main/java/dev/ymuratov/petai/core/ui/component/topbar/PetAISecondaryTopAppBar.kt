package dev.ymuratov.petai.core.ui.component.topbar

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.LocalTextStyle
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import dev.ymuratov.petai.core.ui.theme.PetAITheme

@Composable
fun PetAISecondaryTopAppBar(
    modifier: Modifier = Modifier,
    title: @Composable (BoxScope.() -> Unit)? = null,
    endAction: @Composable (BoxScope.() -> Unit)? = null,
    backgroundColor: Color = Color.Transparent
) {
    Row(
        modifier = modifier.fillMaxWidth().defaultMinSize(minHeight = 40.dp).background(backgroundColor)
            .padding(horizontal = 16.dp, vertical = 4.dp), verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier.weight(1f).defaultMinSize(minHeight = 40.dp), propagateMinConstraints = true,
            contentAlignment = Alignment.CenterStart,
        ) {
            title?.let {
                CompositionLocalProvider(
                    LocalTextStyle provides PetAITheme.typography.titleBlack,
                    LocalContentColor provides PetAITheme.colors.textPrimary,
                ) {
                    title()
                }
            }
        }
        Box(
            modifier = Modifier.defaultMinSize(minHeight = 56.dp),
            propagateMinConstraints = true,
            contentAlignment = Alignment.CenterEnd,
        ) {
            endAction?.let { endAction() }
        }
    }
}