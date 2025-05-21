package me.yeahapps.mypetai.core.ui.component.button.icon

import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import me.yeahapps.mypetai.core.ui.theme.PetAITheme

object PetAIIconButtonDefaults {

    val IconSize = 24.dp
    val Shape = CircleShape

    @Composable
    @Stable
    fun colors(
        containerColor: Color = Color.White,
        disabledContainerColor: Color = Color.Unspecified,
        contentColor: Color = PetAITheme.colors.iconButtonOnPrimary,
        disabledContentColor: Color = Color.Unspecified,
        rippleColor: Color = Color.Unspecified
    ): PetAIIconButtonColors = PetAIIconButtonColors(
        containerColor = containerColor,
        disabledContainerColor = disabledContainerColor,
        contentColor = contentColor,
        disabledContentColor = disabledContentColor,
        rippleColor = rippleColor
    )
}

data class PetAIIconButtonColors(
    val containerColor: Color = Color.Unspecified,
    val disabledContainerColor: Color = Color.Unspecified,
    val contentColor: Color = Color.Unspecified,
    val disabledContentColor: Color = Color.Unspecified,
    val rippleColor: Color = Color.Unspecified,
) {
    @Stable
    fun containerColor(enabled: Boolean): Color = if (enabled) containerColor else disabledContainerColor

    @Stable
    fun contentColor(enabled: Boolean): Color = if (enabled) contentColor else disabledContentColor
}