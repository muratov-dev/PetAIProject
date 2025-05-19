package dev.ymuratov.petai.core.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import dev.ymuratov.petai.feature.discover.domain.model.DiscoverNavType
import dev.ymuratov.petai.feature.discover.domain.model.SongModel
import dev.ymuratov.petai.feature.discover.ui.screen.SongInfoContainer
import dev.ymuratov.petai.feature.discover.ui.screen.SongInfoScreen
import dev.ymuratov.petai.feature.profile.ui.screen.MyWorksContainer
import dev.ymuratov.petai.feature.profile.ui.screen.MyWorksScreen
import dev.ymuratov.petai.feature.root.ui.screen.RootContainer
import dev.ymuratov.petai.feature.root.ui.screen.RootScreen
import kotlin.reflect.typeOf

@Composable
fun PetAINavHost(startDestination: Any, navController: NavHostController, modifier: Modifier = Modifier) {
    NavHost(navController = navController, startDestination = startDestination, modifier = modifier) {
        composable<RootScreen> {
            RootContainer(modifier = Modifier.commonModifier(), parentNavController = navController)
        }
        composable<SongInfoScreen>(typeMap = mapOf(typeOf<SongModel>() to DiscoverNavType.SongType)) {
            SongInfoContainer(modifier = Modifier.commonModifier(), navigateUp = {
                navController.navigateUp()
            })
        }
        composable<MyWorksScreen> {
            MyWorksContainer(modifier = Modifier.commonModifier())
        }
    }
}