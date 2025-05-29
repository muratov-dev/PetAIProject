package me.yeahapps.mypetai.feature.root.ui.component

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.ui.graphics.Color
import me.yeahapps.mypetai.core.ui.theme.PetAITheme

object PetAINavBarDefaults {
    @Composable
    @Stable
    fun colors(
        containerColorPrimary: Color = PetAITheme.colors.bottomNavBarContainerPrimary,
        containerColorSecondary: Color = PetAITheme.colors.bottomNavBarContainerSecondary,
        iconColor: Color = PetAITheme.colors.bottomNavBarIcon,
        selectedIconColor: Color = PetAITheme.colors.bottomNavBarIconSelected,
        textColor: Color = PetAITheme.colors.bottomNavBarIcon,
        selectedTextColor: Color = PetAITheme.colors.bottomNavBarIconSelected,
    ): PetAINavBarColors = PetAINavBarColors(
        containerColorPrimary = containerColorPrimary,
        containerColorSecondary = containerColorSecondary,
        iconColor = iconColor,
        selectedIconColor = selectedIconColor,
        textColor = textColor,
        selectedTextColor = selectedTextColor,

        )
}

data class PetAINavBarColors(
    val containerColorPrimary: Color = Color.Unspecified,
    val containerColorSecondary: Color = Color.Unspecified,
    val iconColor: Color = Color.Unspecified,
    val selectedIconColor: Color = Color.Unspecified,
    val textColor: Color = Color.Unspecified,
    val selectedTextColor: Color = Color.Unspecified,
) {

    @Stable
    fun iconColor(selected: Boolean): Color = if (selected) selectedIconColor else iconColor

    @Stable
    fun textColor(selected: Boolean): Color = if (selected) selectedTextColor else textColor
}
