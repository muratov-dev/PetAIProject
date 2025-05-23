package me.yeahapps.mypetai.core.ui.component.button.filled

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import me.yeahapps.mypetai.core.ui.theme.PetAITheme

object PetAIButtonDefaults {

    val MinHeight = 62.dp
    val Shape = RoundedCornerShape(100.dp)

    @Composable
    fun colors(
        containerColor: Color = PetAITheme.colors.buttonPrimaryDefault,
        disabledContainerColor: Color = PetAITheme.colors.buttonPrimaryDisabled,
        contentColor: Color = PetAITheme.colors.buttonTextPrimary,
        disabledContentColor: Color = PetAITheme.colors.buttonTextPrimary,
        rippleColor: Color = Color.Unspecified
    ) = PetAIButtonColors(
        containerColor = containerColor,
        disabledContainerColor = disabledContainerColor,
        contentColor = contentColor,
        disabledContentColor = disabledContentColor,
        rippleColor = rippleColor,
    )
}

data class PetAIButtonColors(
    val containerColor: Color = Color.Unspecified,
    val disabledContainerColor: Color = Color.Unspecified,
    val contentColor: Color = Color.Unspecified,
    val disabledContentColor: Color = Color.Unspecified,
    val rippleColor: Color = Color.Unspecified,
) {
    @Stable
    fun containerColor(enabled: Boolean): Color = when {
        !enabled -> disabledContainerColor
        else -> containerColor
    }

    @Stable
    fun contentColor(enabled: Boolean): Color = if (enabled) contentColor else disabledContentColor
}