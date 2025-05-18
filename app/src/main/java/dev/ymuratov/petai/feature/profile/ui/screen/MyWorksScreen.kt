package dev.ymuratov.petai.feature.profile.ui.screen

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dev.ymuratov.petai.R
import dev.ymuratov.petai.core.ui.component.PetAIAsyncImage
import dev.ymuratov.petai.core.ui.theme.PetAITheme
import dev.ymuratov.petai.feature.profile.domain.model.MyWorkModel
import dev.ymuratov.petai.feature.profile.ui.event.MyWorksEvent
import dev.ymuratov.petai.feature.profile.ui.state.MyWorksState
import dev.ymuratov.petai.feature.profile.ui.viewmodel.MyWorksViewModel
import kotlinx.serialization.Serializable
import kotlin.random.Random

@Serializable
object MyWorksScreen

@Composable
fun MyWorksContainer(modifier: Modifier = Modifier, viewModel: MyWorksViewModel = hiltViewModel()) {
    val state by viewModel.viewState.collectAsStateWithLifecycle()

    MyWorksContent(modifier = modifier, state = state, onEvent = remember { { event -> viewModel.obtainEvent(event) } })
}

@Composable
private fun MyWorksContent(modifier: Modifier = Modifier, state: MyWorksState, onEvent: (MyWorksEvent) -> Unit) {
    val gridState = rememberLazyGridState()
    Scaffold(modifier = modifier) { innerPadding ->
        Box(modifier = Modifier.padding(top = innerPadding.calculateTopPadding()).fillMaxSize()) {
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
                    modifier = Modifier.matchParentSize().padding(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(state.works) {
                        SongCard(song = it)
                    }
                }
            }
        }
    }
}

@Composable
private fun SongCard(modifier: Modifier = Modifier, song: MyWorkModel) {
    Box(modifier = modifier.size(width = 168.dp, height = 208.dp).clip(RoundedCornerShape(32.dp))) {
        PetAIAsyncImage(modifier = Modifier.matchParentSize(), data = song.imageUrl)
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
            text = song.title,
            style = PetAITheme.typography.textMedium,
            color = PetAITheme.colors.textPrimary,
            modifier = Modifier.padding(16.dp).align(Alignment.BottomStart)
        )
    }
}