package me.yeahapps.mypetai.feature.root.ui.component

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.chrisbanes.haze.HazeState
import dev.chrisbanes.haze.hazeEffect
import me.yeahapps.mypetai.core.ui.theme.PetAITheme
import me.yeahapps.mypetai.feature.root.ui.BottomNavigationItem

@Composable
fun PetAIBottomNavigation(
    hazeState: HazeState,
    items: List<BottomNavigationItem>,
    selectedItem: Int,
    onItemClick: (Int) -> Unit,
) {
    val primaryBottomNavColor = PetAITheme.colors.bottomNavBarContainerPrimary
    val secondaryBottomNavColor = PetAITheme.colors.bottomNavBarContainerSecondary
    Box(modifier = Modifier.fillMaxWidth().hazeEffect(hazeState)) {
        Canvas(modifier = Modifier.matchParentSize()) {
            drawRect(color = primaryBottomNavColor)
            drawRect(color = secondaryBottomNavColor)
        }
        Row(modifier = Modifier.fillMaxWidth().navigationBarsPadding().padding(top = 8.dp, bottom = 4.dp)) {
            items.forEachIndexed { index, item ->
                PetAINavBarItem(
                    icon = item.icon,
                    label = item.label,
                    onClick = { onItemClick(index) },
                    modifier = Modifier.weight(1f),
                    selected = selectedItem == index,
                )
            }
        }
    }
}