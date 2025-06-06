package me.yeahapps.mypetai.feature.profile.ui.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.serialization.Serializable
import me.yeahapps.mypetai.R
import me.yeahapps.mypetai.core.ui.component.SongCard
import me.yeahapps.mypetai.core.ui.component.button.icon.PetAIIconButton
import me.yeahapps.mypetai.core.ui.component.topbar.PetAITopAppBar
import me.yeahapps.mypetai.core.ui.component.topbar.PetAITopBarTitleText
import me.yeahapps.mypetai.core.ui.theme.PetAITheme
import me.yeahapps.mypetai.core.ui.utils.collectFlowWithLifecycle
import me.yeahapps.mypetai.feature.profile.ui.action.MyWorksAction
import me.yeahapps.mypetai.feature.profile.ui.event.MyWorksEvent
import me.yeahapps.mypetai.feature.profile.ui.state.MyWorksState
import me.yeahapps.mypetai.feature.profile.ui.viewmodel.MyWorksViewModel

@Serializable
object MyWorksScreen

@Composable
fun MyWorksContainer(
    modifier: Modifier = Modifier,
    viewModel: MyWorksViewModel = hiltViewModel(),
    navigateToInfo: (Long) -> Unit = {},
    navigateUp: () -> Unit = {}
) {
    val state by viewModel.viewState.collectAsStateWithLifecycle()
    viewModel.viewActions.collectFlowWithLifecycle(viewModel) { action ->
        when (action) {
            MyWorksAction.NavigateUp -> navigateUp()
            is MyWorksAction.NavigateToInfo -> navigateToInfo(action.id)
            null -> {}
        }
    }
    MyWorksContent(
        modifier = modifier.systemBarsPadding(),
        state = state,
        onEvent = remember { { event -> viewModel.obtainEvent(event) } })
}

@Composable
private fun MyWorksContent(modifier: Modifier = Modifier, state: MyWorksState, onEvent: (MyWorksEvent) -> Unit) {
    val gridState = rememberLazyGridState()
    Scaffold(modifier = modifier, topBar = {
        PetAITopAppBar(title = { PetAITopBarTitleText(text = "My works") }, navigationIcon = {
            PetAIIconButton(icon = R.drawable.ic_arrow_left, onClick = { onEvent(MyWorksEvent.NavigateUp) })
        })
    }) { innerPadding ->
        Box(
            modifier = Modifier
                .padding(top = innerPadding.calculateTopPadding())
                .fillMaxSize()
        ) {
            if (state.works.isEmpty()) {
                Column(
                    modifier = Modifier.align(Alignment.Center),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Image(
                        imageVector = ImageVector.vectorResource(R.drawable.ic_works_empty), contentDescription = null
                    )
                    Text(
                        text = "No art created yet",
                        color = PetAITheme.colors.textPrimary.copy(alpha = 0.5f),
                        style = PetAITheme.typography.headlineMedium
                    )
                }
            } else {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    state = gridState,
                    modifier = Modifier
                        .matchParentSize()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(state.works) {
                        SongCard(song = it) {
                            onEvent(MyWorksEvent.NavigateToInfo(it.id.toLong()))
                        }
                    }
                }
            }
        }
    }
}