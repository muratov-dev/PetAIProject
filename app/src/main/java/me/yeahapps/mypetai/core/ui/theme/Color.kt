package me.yeahapps.mypetai.core.ui.theme

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

@Immutable
data class PetAIColors(
    val textPrimary: Color = Color.Unspecified,

    val backgroundPrimary: Color = Color.Unspecified,
    val buttonSecondaryDefault: Color = Color.Unspecified,

    val buttonPrimaryDefault: Color = Color.Unspecified,
    val buttonPrimaryDisabled: Color = Color.Unspecified,

    val buttonTextPrimary: Color = Color.Unspecified,
    val buttonTextSecondary: Color = Color.Unspecified,

    val iconButtonOnPrimary: Color = Color.Unspecified,

    val bottomNavBarContainerPrimary: Color = Color.Unspecified,
    val bottomNavBarContainerSecondary: Color = Color.Unspecified,
    val bottomNavBarIcon: Color = Color.Unspecified,
    val bottomNavBarIconSelected: Color = Color.Unspecified,
    val bottomNavBarText: Color = Color.Unspecified,
    val bottomNavBarTextSelected: Color = Color.Unspecified,
) {

    companion object {
        @Stable
        val Dark = PetAIColors(
            textPrimary = Color(0xFFFFFFFF),

            backgroundPrimary = Color(0xFF040400),

            buttonPrimaryDefault = Color(0xFFC2FD54),
            buttonPrimaryDisabled = Color(0x40C2FD54),

            buttonSecondaryDefault = Color(0x19FFFFFF),

            buttonTextPrimary = Color(0xFF000000),
            buttonTextSecondary = Color(0xFFFFFFFF),

            iconButtonOnPrimary = Color(0xFFFFFFFF),

            bottomNavBarContainerPrimary = Color(0xFF7C7C7C),
            bottomNavBarContainerSecondary = Color(0xE50A0905),
            bottomNavBarIcon = Color(0xFF7E7E7E),
            bottomNavBarIconSelected = Color(0xFFC2FD54),
            bottomNavBarText = Color(0xFF7E7E7E),
            bottomNavBarTextSelected = Color(0xFFC2FD54),
        )
    }
}

internal val LocalPetAIColors = staticCompositionLocalOf { PetAIColors() }