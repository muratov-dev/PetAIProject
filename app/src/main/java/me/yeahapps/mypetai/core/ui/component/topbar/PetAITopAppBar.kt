package me.yeahapps.mypetai.core.ui.component.topbar

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
import me.yeahapps.mypetai.core.ui.theme.PetAITheme

@Composable
fun PetAITopAppBar(
    modifier: Modifier = Modifier,
    title: @Composable (BoxScope.() -> Unit)? = null,
    navigationIcon: @Composable (() -> Unit)? = null,
    actions: @Composable (RowScope.() -> Unit)? = null,
) {
    Row(
        modifier = modifier.fillMaxWidth().defaultMinSize(minHeight = 56.dp).background(color = Color.Transparent),
        verticalAlignment = Alignment.CenterVertically
    ) {
        val iconContainerModifier = Modifier.padding(horizontal = 8.dp).size(40.dp)

        navigationIcon?.let {
            Box(modifier = iconContainerModifier, propagateMinConstraints = true) { navigationIcon() }
        } ?: Spacer(modifier = iconContainerModifier)

        Box(
            modifier = Modifier.weight(1f).defaultMinSize(minHeight = 56.dp),
            propagateMinConstraints = true,
            contentAlignment = Alignment.Center
        ) {
            title?.let {
                CompositionLocalProvider(
                    LocalTextStyle provides PetAITheme.typography.headlineMedium,
                    LocalContentColor provides PetAITheme.colors.textPrimary,
                ) {
                    title()
                }
            }
        }

        actions?.let {
            Box(modifier = iconContainerModifier, propagateMinConstraints = true) { Row { actions() } }
        } ?: Spacer(modifier = iconContainerModifier)
    }
}