package dev.ymuratov.petai.core.ui.theme

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

@Immutable
data class PetAIColors(
    val textPrimary: Color = Color.Unspecified,

    val backgroundPrimary: Color = Color.Unspecified,

    val buttonPrimaryDefault: Color = Color.Unspecified,
    val buttonTextPrimary: Color = Color.Unspecified,
) {

    companion object {
        @Stable
        val Dark = PetAIColors(
            textPrimary = Color(0xFFFFFFFF),

            backgroundPrimary = Color(0xFF040400),

            buttonPrimaryDefault = Color(0xFFC2FD54),
            buttonTextPrimary = Color(0xFF000000),
        )
    }
}

internal val LocalPetAIColors = staticCompositionLocalOf { PetAIColors() }