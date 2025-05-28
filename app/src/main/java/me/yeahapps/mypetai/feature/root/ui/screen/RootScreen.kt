package me.yeahapps.mypetai.feature.root.ui.screen

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
import kotlinx.serialization.Serializable
import me.yeahapps.mypetai.core.ui.navigation.commonModifier
import me.yeahapps.mypetai.feature.create.ui.screen.AudioRecordScreen
import me.yeahapps.mypetai.feature.create.ui.screen.CreateContainer
import me.yeahapps.mypetai.feature.create.ui.screen.VideoProcessingScreen
import me.yeahapps.mypetai.feature.discover.ui.screen.DiscoverContainer
import me.yeahapps.mypetai.feature.discover.ui.screen.SongInfoScreen
import me.yeahapps.mypetai.feature.profile.ui.screen.MyWorksScreen
import me.yeahapps.mypetai.feature.profile.ui.screen.ProfileContainer
import me.yeahapps.mypetai.feature.root.ui.BottomNavigationItem
import me.yeahapps.mypetai.feature.root.ui.component.PetAIBottomNavigation

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
            navController, startDestination = BottomNavigationItem.Discover.route, modifier = Modifier.fillMaxSize()
        ) {
            composable(BottomNavigationItem.Discover.route) {
                DiscoverContainer(
                    modifier = Modifier
                        .commonModifier()
                        .padding(bottom = innerPadding.calculateBottomPadding()),
                    navigateToCreate = { navController.navigate(BottomNavigationItem.Create.route) },
                    navigateToSongInfo = {
                        parentNavController.navigate(SongInfoScreen(it))
                    })
            }
            composable(BottomNavigationItem.Create.route) {
                CreateContainer(modifier = Modifier.commonModifier(), navigateToProcessing = { imageUri, audioUri ->
                    parentNavController.navigate(VideoProcessingScreen(imageUri = imageUri, audioUri = audioUri))
                }, navigateToRecord = { parentNavController.navigate(AudioRecordScreen) })
            }
            composable(BottomNavigationItem.Profile.route) {
                ProfileContainer(modifier = Modifier.commonModifier(), navigateToMyWorks = {
                    parentNavController.navigate(MyWorksScreen)
                })
            }
        }
    }
}