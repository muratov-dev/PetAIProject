package dev.ymuratov.petai.core.ui.theme

import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider


@Composable
fun PetAITheme(content: @Composable () -> Unit) {
    val petAIColors = PetAIColors.Dark
    val petAITypography = PetAITypography()

    val defaultTextStyle = petAITypography.textRegular

    CompositionLocalProvider(
        LocalPetAIColors provides petAIColors,
        LocalPetAITypography provides petAITypography,
        LocalTextStyle provides defaultTextStyle,
    ) {
        MaterialTheme(
            colorScheme = darkColorScheme(
                onSurface = PetAITheme.colors.textPrimary,
                surface = PetAITheme.colors.backgroundPrimary,
                background = PetAITheme.colors.backgroundPrimary,
                primary = PetAITheme.colors.buttonPrimaryDefault,
                onPrimary = PetAITheme.colors.buttonTextPrimary,
            ), typography = Typography(), content = content
        )
    }
}

object PetAITheme {
    val colors: PetAIColors
        @Composable get() = LocalPetAIColors.current

    val typography: PetAITypography
        @Composable get() = LocalPetAITypography.current
}
