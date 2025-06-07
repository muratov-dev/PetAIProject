package me.yeahapps.mypetai.feature.discover.ui.screen

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dev.chrisbanes.haze.hazeEffect
import dev.chrisbanes.haze.hazeSource
import dev.chrisbanes.haze.rememberHazeState
import kotlinx.coroutines.delay
import me.yeahapps.mypetai.R
import me.yeahapps.mypetai.core.ui.component.button.GetProButton
import me.yeahapps.mypetai.core.ui.component.topbar.PetAISecondaryTopAppBar
import me.yeahapps.mypetai.core.ui.navigation.commonModifier
import me.yeahapps.mypetai.core.ui.theme.PetAITheme
import me.yeahapps.mypetai.core.ui.utils.collectFlowWithLifecycle
import me.yeahapps.mypetai.feature.discover.domain.model.SongModel
import me.yeahapps.mypetai.feature.discover.ui.action.DiscoverAction
import me.yeahapps.mypetai.feature.discover.ui.component.CategoryItem
import me.yeahapps.mypetai.feature.discover.ui.component.CategoryItemPlaceholder
import me.yeahapps.mypetai.feature.discover.ui.component.DiscoverAllSongsBottomSheet
import me.yeahapps.mypetai.feature.discover.ui.component.TryNowButton
import me.yeahapps.mypetai.feature.discover.ui.event.DiscoverEvent
import me.yeahapps.mypetai.feature.discover.ui.state.DiscoverState
import me.yeahapps.mypetai.feature.discover.ui.viewmodel.DiscoverViewModel
import me.yeahapps.mypetai.feature.subscription.ui.screen.SubscriptionsContainer

@Composable
fun DiscoverContainer(
    modifier: Modifier = Modifier,
    viewModel: DiscoverViewModel = hiltViewModel(),
    navigateToSubscriptions: () -> Unit,
    navigateToCreate: () -> Unit,
    navigateToSongInfo: (SongModel) -> Unit
) {
    val state by viewModel.viewState.collectAsStateWithLifecycle()
    viewModel.viewActions.collectFlowWithLifecycle(viewModel) { action ->
        when (action) {
            is DiscoverAction.NavigateToSongInfo -> navigateToSongInfo(action.song)
            is DiscoverAction.NavigateToSubscriptions -> navigateToSubscriptions()
            is DiscoverAction.NavigateToCreate -> navigateToCreate()
            null -> {}
        }
    }

    DiscoverContent(
        modifier = modifier,
        state = state,
        onEvent = remember { { event -> viewModel.obtainEvent(event) } })
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DiscoverContent(
    modifier: Modifier = Modifier,
    state: DiscoverState = DiscoverState(),
    onEvent: (DiscoverEvent) -> Unit = {},
) {
    var isButtonExpanded by remember { mutableStateOf(true) }
    var showBottomSheet by remember { mutableStateOf(false) }

    val hazeState = rememberHazeState(true)
    val discoverListState = rememberLazyListState()
    val sheetState = rememberModalBottomSheetState()

    //TODO Добавить анимацию затемнения при скролле
    val isCollapsed by remember { derivedStateOf { discoverListState.firstVisibleItemIndex > 0 } }
    val animatedColor by animateColorAsState(
        targetValue = if (isCollapsed) PetAITheme.colors.backgroundPrimary else Color.Transparent,
        label = "",
        animationSpec = tween(300)
    )

    LaunchedEffect(isButtonExpanded) {
        if (isButtonExpanded) {
            delay(5000)
            isButtonExpanded = false
        }
    }

    Box(modifier = modifier.fillMaxSize()) {
        LazyColumn(
            state = discoverListState,
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            item {
                Box(modifier = Modifier.fillMaxWidth()) {
                    Image(
                        painter = painterResource(R.drawable.im_discover_main),
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .fillMaxWidth()
                            .hazeSource(hazeState)
                    )
                    Canvas(
                        modifier = Modifier
                            .matchParentSize()
                            .hazeSource(hazeState), onDraw = {
                            drawRect(
                                Brush.verticalGradient(listOf(Color(0xA60A0701), Color(0x40040401), Color(0xFF040400)))
                            )
                        })
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .align(Alignment.BottomCenter),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = stringResource(R.string.discover_info_title),
                            color = PetAITheme.colors.textPrimary,
                            style = PetAITheme.typography.headlineSemiBold.copy(fontSize = 22.sp)
                        )
                        Spacer(Modifier.size(6.dp))
                        Text(
                            text = stringResource(R.string.discover_info_message),
                            color = PetAITheme.colors.textPrimary.copy(alpha = 0.7f),
                            style = PetAITheme.typography.textRegular
                        )
                        Spacer(Modifier.size(20.dp))
                        TryNowButton(Modifier.hazeEffect(hazeState)) { onEvent(DiscoverEvent.NavigateToCreate) }
                    }
                }
            }

            if (state.songs.isEmpty()) item { CategoryItemPlaceholder() }
            else {
                items(state.songCategories, key = { it.id }) { category ->
                    val filteredSongs = state.songs.filter { it.songCategories.contains(category.name) }
                    if (filteredSongs.isEmpty()) return@items
                    CategoryItem(
                        category = category,
                        songs = filteredSongs,
                        onSongClick = { onEvent(DiscoverEvent.NavigateToSongInfo(it)) }) {
                        showBottomSheet = true
                    }
                }
            }
        }
        PetAISecondaryTopAppBar(backgroundColor = animatedColor, title = {
            Text(
                text = stringResource(R.string.app_name_secondary),
                color = Color.White,
                style = PetAITheme.typography.titleBlack,
                modifier = Modifier
                    .wrapContentHeight(Alignment.CenterVertically)
                    .statusBarsPadding()
            )
        }, endAction = {
            GetProButton(
                modifier = Modifier
                    .wrapContentSize()
                    .statusBarsPadding()
            ) {
                if (isButtonExpanded) onEvent(DiscoverEvent.NavigateToSubscriptions)
                else isButtonExpanded = true
            }
        })
        FloatingActionButton(
            onClick = { onEvent(DiscoverEvent.NavigateToCreate) },
            shape = CircleShape,
            modifier = Modifier
                .padding(bottom = 24.dp)
                .align(Alignment.BottomCenter)
                .padding(16.dp),
            contentColor = PetAITheme.colors.buttonTextPrimary,
            containerColor = PetAITheme.colors.buttonPrimaryDefault
        ) {
            Icon(
                imageVector = ImageVector.vectorResource(R.drawable.ic_plus),
                contentDescription = null,
                modifier = Modifier.size(16.dp)
            )
        }
        if (showBottomSheet) {
            DiscoverAllSongsBottomSheet(
                sheetState = sheetState,
                selectedCategory = state.selectedCategory,
                categories = state.bottomSheetCategories,
                songs = state.songs,
                onDismissRequest = { showBottomSheet = false },
                onCategorySelect = { onEvent(DiscoverEvent.SelectCategory(it)) },
                onSongClick = { onEvent(DiscoverEvent.NavigateToSongInfo(it)) })
        }
    }
}