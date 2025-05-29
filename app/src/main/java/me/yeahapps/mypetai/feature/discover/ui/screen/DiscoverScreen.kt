package me.yeahapps.mypetai.feature.discover.ui.screen

import androidx.activity.compose.LocalActivity
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.displayCutoutPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dev.chrisbanes.haze.hazeEffect
import dev.chrisbanes.haze.hazeSource
import dev.chrisbanes.haze.rememberHazeState
import kotlinx.coroutines.delay
import kotlinx.serialization.Serializable
import me.yeahapps.mypetai.R
import me.yeahapps.mypetai.core.ui.component.topbar.PetAISecondaryTopAppBar
import me.yeahapps.mypetai.core.ui.theme.PetAITheme
import me.yeahapps.mypetai.core.ui.utils.collectFlowWithLifecycle
import me.yeahapps.mypetai.feature.discover.domain.model.SongModel
import me.yeahapps.mypetai.feature.discover.ui.action.DiscoverAction
import me.yeahapps.mypetai.feature.discover.ui.component.CategoryItem
import me.yeahapps.mypetai.feature.discover.ui.component.CategoryTagChip
import me.yeahapps.mypetai.feature.discover.ui.component.SongCard
import me.yeahapps.mypetai.feature.discover.ui.event.DiscoverEvent
import me.yeahapps.mypetai.feature.discover.ui.state.DiscoverState
import me.yeahapps.mypetai.feature.discover.ui.viewmodel.DiscoverViewModel

@Serializable
object DiscoverScreen

@Composable
fun DiscoverContainer(
    modifier: Modifier = Modifier,
    viewModel: DiscoverViewModel = hiltViewModel(),
    navigateToCreate: () -> Unit,
    navigateToSongInfo: (SongModel) -> Unit
) {
    val state by viewModel.viewState.collectAsStateWithLifecycle()
    viewModel.viewActions.collectFlowWithLifecycle(viewModel) { action ->
        when (action) {
            is DiscoverAction.NavigateToSongInfo -> navigateToSongInfo(action.song)
            is DiscoverAction.NavigateToCreate -> navigateToCreate()
            null -> {}
        }
    }

    //TODO добавить Flow для вывода списка. Убрать инит в VM
    val lifecycleOwner = LocalLifecycleOwner.current
    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_CREATE -> viewModel.obtainEvent(DiscoverEvent.InitState)
                else -> {}
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    DiscoverContent(
        modifier = modifier, state = state, onEvent = remember { { event -> viewModel.obtainEvent(event) } })
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DiscoverContent(
    modifier: Modifier = Modifier, state: DiscoverState = DiscoverState(), onEvent: (DiscoverEvent) -> Unit = {},
) {
    val activity = LocalActivity.current
    var isButtonExpanded by remember { mutableStateOf(true) }
    val hazeState = rememberHazeState(true)
    val listState = rememberLazyListState()
    val gridState = rememberLazyGridState()
    //TODO Добавить анимацию затемнения при скролле
    val isCollapsed by remember { derivedStateOf { listState.firstVisibleItemIndex > 0 } }
    val animatedColor by animateColorAsState(
        targetValue = if (isCollapsed) PetAITheme.colors.backgroundPrimary else Color.Transparent,
        label = "",
        animationSpec = tween(300)
    )

    //TODO Скрыть на время кнопку
    LaunchedEffect(isButtonExpanded) {
        if (isButtonExpanded) {
            delay(5000)
            isButtonExpanded = false
        }
    }

    val sheetState = rememberModalBottomSheetState()
    var showBottomSheet by remember { mutableStateOf(false) }

    Box(modifier = modifier.fillMaxSize()) {
        LazyColumn(
            state = listState, modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.spacedBy(20.dp)
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
                    //TODO Вынести
                    Canvas(
                        modifier = Modifier
                            .matchParentSize()
                            .hazeSource(hazeState), onDraw = {
                            drawRect(
                                Brush.verticalGradient(
                                    listOf(
                                        Color(0xA60A0701), Color(0x40040401), Color(0xFF040400)
                                    )
                                )
                            )
                        })
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .align(Alignment.BottomCenter),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        //TODO Строки сделать
                        Text(
                            text = "VocalPet",
                            color = PetAITheme.colors.textPrimary,
                            style = PetAITheme.typography.headlineSemiBold.copy(fontSize = 22.sp)
                        )
                        Spacer(Modifier.size(6.dp))
                        Text(
                            text = "Let your pet speak and bring more fun!",
                            color = PetAITheme.colors.textPrimary.copy(alpha = 0.7f),
                            style = PetAITheme.typography.textRegular
                        )
                        Spacer(Modifier.size(20.dp))
                        //TODO Сделать кнопку рабочей и вынести
                        Button(
                            onClick = { onEvent(DiscoverEvent.NavigateToCreate) },
                            shape = RoundedCornerShape(100.dp),
                            contentPadding = PaddingValues(vertical = 12.dp, horizontal = 40.dp),
                            border = BorderStroke(2.dp, PetAITheme.colors.buttonPrimaryDefault),
                            modifier = Modifier
                                .clip(RoundedCornerShape(100.dp))
                                .hazeEffect(hazeState),
                            colors = ButtonColors(
                                containerColor = PetAITheme.colors.buttonPrimaryDefault.copy(alpha = 0.1f),
                                contentColor = PetAITheme.colors.buttonTextPrimary,
                                disabledContainerColor = Color.White,
                                disabledContentColor = Color.Black
                            ),
                        ) {
                            Text(
                                text = "Try Now",
                                color = PetAITheme.colors.buttonTextSecondary,
                                style = PetAITheme.typography.buttonTextDefault
                            )
                        }
                    }
                }
            }

            //TODO Подумать над тем, как это лучше сделать
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
        PetAISecondaryTopAppBar(
            backgroundColor = animatedColor,
            title = {
                Text(
                    text = "PETTALK",
                    color = Color.White,
                    style = PetAITheme.typography.titleBlack,
                    modifier = Modifier
                        .wrapContentHeight(Alignment.CenterVertically)
                        .systemBarsPadding()
                )
            },
//            endAction = {
//            //TODO Скрыть на время и вынести
//            Button(
//                onClick = {
//                    if (isButtonExpanded) {
//                        activity?.let { onEvent(DiscoverEvent.StartSubscription(it)) }
//                    } else {
//                        isButtonExpanded = true
//                    }
//                },
//                shape = RoundedCornerShape(100.dp),
//                contentPadding = PaddingValues(vertical = 8.dp, horizontal = 16.dp),
//                colors = ButtonColors(
//                    containerColor = PetAITheme.colors.buttonPrimaryDefault,
//                    contentColor = PetAITheme.colors.buttonTextPrimary,
//                    disabledContainerColor = Color.White,
//                    disabledContentColor = Color.Black
//                ),
//                modifier = Modifier
//                    .wrapContentSize()
//                    .statusBarsPadding()
//            ) {
//                Row(
//                    modifier = Modifier.animateContentSize(tween(300)),
//                    verticalAlignment = Alignment.CenterVertically,
//                    horizontalArrangement = Arrangement.spacedBy(4.dp),
//                ) {
//                    Icon(
//                        imageVector = ImageVector.vectorResource(R.drawable.ic_ribbon), contentDescription = null
//                    )
//                    if (isButtonExpanded) {
//                        Text(
//                            text = "Get PRO",
//                            color = PetAITheme.colors.buttonTextPrimary,
//                            style = PetAITheme.typography.buttonTextDefault
//                        )
//                    }
//                }
//            }
//        }
        )
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
        //TODO Вынести
        if (showBottomSheet) {
            ModalBottomSheet(
                modifier = Modifier.displayCutoutPadding(),
                sheetState = sheetState,
                onDismissRequest = { showBottomSheet = false },
                dragHandle = {
                    Canvas(
                        Modifier
                            .padding(top = 8.dp, bottom = 8.dp)
                            .width(36.dp)
                            .height(5.dp)
                            .clip(RoundedCornerShape(100.dp))
                    ) {
                        drawRect(color = Color(0x667F7F7F))
                        drawRect(color = Color(0x80C2C2C2), blendMode = BlendMode.Multiply)
                    }
                },
                containerColor = Color(0xFF221F03),
            ) {
                Column {
                    Spacer(Modifier.size(16.dp))
                    LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        item {
                            Spacer(Modifier.size(8.dp))
                        }
                        item {
                            CategoryTagChip(
                                label = "All",
                                selected = state.selectedCategory == null,
                                onChipClick = { onEvent(DiscoverEvent.SelectCategory(null)) })
                        }
                        items(state.bottomSheetCategories, key = { it.id }) {
                            CategoryTagChip(
                                label = it.name,
                                selected = it == state.selectedCategory,
                                onChipClick = { onEvent(DiscoverEvent.SelectCategory(it)) })
                        }
                        item {
                            Spacer(Modifier.size(8.dp))
                        }
                    }
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(2),
                        state = gridState,
                        modifier = Modifier
                            .weight(1f)
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(
                            if (state.selectedCategory == null) state.songs
                            else state.songs.filter { it.songCategories.contains(state.selectedCategory.name) },
                        ) {
                            SongCard(song = it)
                        }
                    }
                }
            }
        }
    }
}