package dev.ymuratov.petai.feature.discover.ui.screen

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dev.chrisbanes.haze.hazeEffect
import dev.chrisbanes.haze.hazeSource
import dev.chrisbanes.haze.rememberHazeState
import dev.ymuratov.petai.R
import dev.ymuratov.petai.core.ui.component.PetAIAsyncImage
import dev.ymuratov.petai.core.ui.theme.PetAITheme
import dev.ymuratov.petai.feature.discover.domain.model.SongCategoryModel
import dev.ymuratov.petai.feature.discover.domain.model.SongModel
import dev.ymuratov.petai.feature.discover.ui.event.DiscoverEvent
import dev.ymuratov.petai.feature.discover.ui.state.DiscoverState
import dev.ymuratov.petai.feature.discover.ui.viewmodel.DiscoverViewModel
import kotlinx.serialization.Serializable
import kotlin.random.Random

@Serializable
object DiscoverScreen

@Composable
fun DiscoverContainer(modifier: Modifier = Modifier, viewModel: DiscoverViewModel = hiltViewModel()) {
    val state by viewModel.viewState.collectAsStateWithLifecycle()

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
    modifier: Modifier = Modifier, state: DiscoverState = DiscoverState(), onEvent: (DiscoverEvent) -> Unit = {}
) {
    val hazeState = rememberHazeState(true)
    val listState = rememberLazyListState()
    val gridState = rememberLazyGridState()
    val isCollapsed by remember { derivedStateOf { listState.firstVisibleItemIndex > 0 } }
    val animatedColor by animateColorAsState(
        targetValue = if (isCollapsed) PetAITheme.colors.backgroundPrimary else Color.Transparent,
        label = "",
        animationSpec = tween(300)
    )

    val sheetState = rememberModalBottomSheetState()
    val scope = rememberCoroutineScope()
    var showBottomSheet by remember { mutableStateOf(false) }

    Box() {
        LazyColumn(
            state = listState,
            modifier = modifier.fillMaxSize().navigationBarsPadding(),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            item {
                Box(modifier = Modifier.fillMaxWidth()) {
                    Image(
                        painter = painterResource(R.drawable.im_discover_main),
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxWidth().hazeSource(hazeState)
                    )
                    Canvas(modifier = Modifier.matchParentSize().hazeSource(hazeState), onDraw = {
                        drawRect(
                            Brush.verticalGradient(
                                listOf(
                                    Color(0xA60A0701), Color(0x40040401), Color(0xFF040400)
                                )
                            )
                        )
                    })
                    Column(
                        modifier = Modifier.fillMaxWidth().align(Alignment.BottomCenter),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Button(
                            onClick = {}, shape = RoundedCornerShape(100.dp),
                            contentPadding = PaddingValues(vertical = 12.dp, horizontal = 40.dp),
                            border = BorderStroke(2.dp, PetAITheme.colors.buttonPrimaryDefault),
                            modifier = Modifier.clip(RoundedCornerShape(100.dp)).hazeEffect(hazeState),
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
            items(state.songCategories, key = { it.id }) { category ->
                val filteredSongs = state.songs.filter { it.songCategories.contains(category.name) }
                if (filteredSongs.isEmpty()) return@items
                CategoryItem(category = category, songs = filteredSongs) {
                    showBottomSheet = true
                }
            }
        }
        Box(
            modifier = Modifier.fillMaxWidth().background(animatedColor).statusBarsPadding()
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp).fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = "PETTALK", color = Color.White, style = PetAITheme.typography.titleBlack)
                Button(
                    onClick = {}, shape = RoundedCornerShape(100.dp),
                    contentPadding = PaddingValues(vertical = 8.dp, horizontal = 16.dp),
                    colors = ButtonColors(
                        containerColor = PetAITheme.colors.buttonPrimaryDefault,
                        contentColor = PetAITheme.colors.buttonTextPrimary,
                        disabledContainerColor = Color.White,
                        disabledContentColor = Color.Black
                    ),
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                    ) {
                        Icon(
                            imageVector = ImageVector.vectorResource(R.drawable.ic_ribbon), contentDescription = null
                        )
                        Text(
                            text = "Get PRO",
                            color = PetAITheme.colors.buttonTextPrimary,
                            style = PetAITheme.typography.buttonTextDefault
                        )
                    }
                }
            }
        }
        if (showBottomSheet) {
            ModalBottomSheet(
                modifier = Modifier.displayCutoutPadding(),
                sheetState = sheetState,
                onDismissRequest = { showBottomSheet = false },
                dragHandle = {
                    Canvas(
                        Modifier.padding(top = 8.dp, bottom = 8.dp).width(36.dp).height(5.dp)
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
                        modifier = Modifier.weight(1f).padding(16.dp),
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

@Composable
fun CategoryTagChip(modifier: Modifier = Modifier, label: String, selected: Boolean, onChipClick: () -> Unit) {
    Box(
        modifier = modifier.widthIn(min = 64.dp).wrapContentHeight().background(
            color = if (selected) PetAITheme.colors.buttonPrimaryDefault else Color.White.copy(alpha = 0.15f),
            shape = RoundedCornerShape(100.dp)
        ).clip(RoundedCornerShape(100.dp)).clickable(onClick = onChipClick).padding(12.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = label,
            style = PetAITheme.typography.textMedium,
            color = if (selected) PetAITheme.colors.buttonTextPrimary else PetAITheme.colors.buttonTextSecondary
        )
    }
}

@Composable
private fun CategoryItem(
    modifier: Modifier = Modifier,
    category: SongCategoryModel,
    songs: List<SongModel>,
    seeAllClick: (SongCategoryModel) -> Unit = {}
) {
    Column(modifier = modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(20.dp)) {
        Row(
            modifier = Modifier.padding(16.dp).fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = category.name,
                style = PetAITheme.typography.headlineMedium,
                color = PetAITheme.colors.textPrimary,
                modifier = Modifier.weight(1f)
            )
            Text(
                text = stringResource(R.string.common_see_all),
                style = PetAITheme.typography.labelMedium,
                color = PetAITheme.colors.textPrimary.copy(alpha = 0.7f),
                modifier = Modifier.clickable(onClick = { seeAllClick(category) })
            )
        }

        LazyRow(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            item {
                Spacer(Modifier.size(8.dp))
            }
            items(songs, key = { it.id }) { song ->
                SongCard(song = song)
            }
            item {
                Spacer(Modifier.size(8.dp))
            }
        }
    }
}

@Composable
private fun SongCard(modifier: Modifier = Modifier, song: SongModel) {
    Box(modifier = modifier.size(width = 168.dp, height = 208.dp).clip(RoundedCornerShape(32.dp))) {
        PetAIAsyncImage(modifier = Modifier.matchParentSize(), data = song.videos.first().imageUrl)
        Canvas(modifier = Modifier.fillMaxWidth().fillMaxHeight(0.5f).align(Alignment.BottomCenter), onDraw = {
            drawRect(Brush.verticalGradient(listOf(Color.Transparent, Color(0xCC040401))))
        })
        Row(
            modifier = Modifier.padding(16.dp).wrapContentSize().background(
                color = PetAITheme.colors.backgroundPrimary.copy(alpha = 0.5f), shape = RoundedCornerShape(25.dp)
            ).padding(5.dp)
        ) {
            Icon(
                imageVector = ImageVector.vectorResource(R.drawable.ic_music),
                contentDescription = null,
                tint = PetAITheme.colors.textPrimary,
                modifier = Modifier.size(14.dp)
            )
            Spacer(Modifier.size(1.dp))
            val listensCount = Random.nextInt(10, 900)
            Text(
                text = "${listensCount}k",
                style = PetAITheme.typography.labelMedium,
                color = PetAITheme.colors.textPrimary
            )
        }

        Text(
            text = song.name,
            style = PetAITheme.typography.textMedium,
            color = PetAITheme.colors.textPrimary,
            modifier = Modifier.padding(16.dp).align(Alignment.BottomStart)
        )
    }
}