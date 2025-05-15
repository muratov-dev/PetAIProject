package dev.ymuratov.petai.feature.discover.ui.screen

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
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
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
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

@Composable
private fun DiscoverContent(
    modifier: Modifier = Modifier, state: DiscoverState = DiscoverState(), onEvent: (DiscoverEvent) -> Unit = {}
) {
    val listState = rememberLazyListState()
    val isCollapsed by remember { derivedStateOf { listState.firstVisibleItemIndex > 0 } }
    val animatedColor by animateColorAsState(
        targetValue = if (isCollapsed) PetAITheme.colors.backgroundPrimary else Color.Transparent,
        label = "",
        animationSpec = tween(300)
    )
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
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
            items(state.songCategories, key = { it.id }) { category ->
                val filteredSongs = state.songs.filter { it.songCategories.contains(category.name) }
                if (filteredSongs.isEmpty()) return@items
                CategoryItem(category = category, songs = filteredSongs)
            }
        }
        Box(
            modifier = Modifier.fillMaxWidth().background(animatedColor).statusBarsPadding()
        ) {
            Row(modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp).fillMaxWidth()) {
                Text(
                    text = "PETTALK",
                    color = Color.White,
                    style = PetAITheme.typography.headlineMedium.copy(fontSize = 24.sp)
                )
            }
        }
    }
}

@Composable
private fun CategoryItem(modifier: Modifier = Modifier, category: SongCategoryModel, songs: List<SongModel>) {
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
                color = PetAITheme.colors.textPrimary.copy(alpha = 0.7f)
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