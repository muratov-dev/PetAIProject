package dev.ymuratov.petai.feature.root.ui.screen

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import dev.chrisbanes.haze.hazeSource
import dev.chrisbanes.haze.rememberHazeState
import dev.ymuratov.petai.core.ui.navigation.commonModifier
import dev.ymuratov.petai.feature.create.ui.screen.CreateContainer
import dev.ymuratov.petai.feature.discover.ui.screen.DiscoverContainer
import dev.ymuratov.petai.feature.profile.ui.screen.MyWorksScreen
import dev.ymuratov.petai.feature.profile.ui.screen.ProfileContainer
import dev.ymuratov.petai.feature.root.ui.BottomNavigationItem
import dev.ymuratov.petai.feature.root.ui.component.PetAIBottomNavigation
import kotlinx.serialization.Serializable

@Serializable
object RootScreen

@Composable
fun RootContainer(modifier: Modifier = Modifier, parentNavController: NavHostController) {
    RootContent(modifier = modifier, parentNavController = parentNavController)
}

@Composable
private fun RootContent(modifier: Modifier = Modifier, parentNavController: NavHostController) {
    val items = listOf(BottomNavigationItem.Discover, BottomNavigationItem.Create, BottomNavigationItem.Profile)

    val hazeState = rememberHazeState(blurEnabled = true)
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestinationIndex = navBackStackEntry?.destination?.route?.let { route ->
        items.indexOfFirst { it.route == route }
    } ?: 0

    Scaffold(modifier = modifier.hazeSource(hazeState), bottomBar = {
        PetAIBottomNavigation(
            hazeState = hazeState, items = items, selectedItem = currentDestinationIndex, onItemClick = {
                navController.navigate(items[it].route) {
                    launchSingleTop = true
                    restoreState = true
                }
            })
    }) { innerPadding ->
        NavHost(
            navController,
            startDestination = BottomNavigationItem.Discover.route,
            modifier = Modifier.fillMaxSize().padding(top = innerPadding.calculateTopPadding())
        ) {
            composable(BottomNavigationItem.Discover.route) {
                DiscoverContainer(modifier = Modifier.commonModifier())
            }
            composable(BottomNavigationItem.Create.route) {
                CreateContainer(modifier = Modifier.commonModifier())
            }
            composable(BottomNavigationItem.Profile.route) {
                ProfileContainer(modifier = Modifier.commonModifier(), navigateToMyWorks = {
                    parentNavController.navigate(MyWorksScreen)
                })
            }
        }
    }
}