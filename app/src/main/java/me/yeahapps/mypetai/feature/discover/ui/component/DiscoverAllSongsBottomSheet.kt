package me.yeahapps.mypetai.feature.discover.ui.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.displayCutoutPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import me.yeahapps.mypetai.core.ui.component.SongCard
import me.yeahapps.mypetai.core.ui.component.bottomsheet.PetAIDragHandle
import me.yeahapps.mypetai.feature.discover.domain.model.SongCategoryModel
import me.yeahapps.mypetai.feature.discover.domain.model.SongModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DiscoverAllSongsBottomSheet(
    sheetState: SheetState,
    modifier: Modifier = Modifier,
    selectedCategory: SongCategoryModel? = null,
    categories: List<SongCategoryModel> = emptyList(),
    songs: List<SongModel> = emptyList(),
    onDismissRequest: () -> Unit = {},
    onCategorySelect: (SongCategoryModel?) -> Unit = {},
    onSongClick: (SongModel) -> Unit = {}
) {
    val lazyGridState = rememberLazyGridState()

    ModalBottomSheet(
        modifier = modifier.displayCutoutPadding(),
        sheetState = sheetState,
        onDismissRequest = onDismissRequest,
        dragHandle = { PetAIDragHandle() },
        containerColor = Color(0xFF221F03),
    ) {
        Column {
            Spacer(Modifier.size(16.dp))
            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                item { Spacer(Modifier.size(8.dp)) }
                item {
                    CategoryTagChip(
                        label = "All", selected = selectedCategory == null, onChipClick = { onCategorySelect(null) })
                }
                items(categories, key = { it.id }) {
                    CategoryTagChip(
                        label = it.name, selected = it == selectedCategory, onChipClick = { onCategorySelect(it) })
                }
                item {
                    Spacer(Modifier.size(8.dp))
                }
            }
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                state = lazyGridState,
                modifier = Modifier
                    .weight(1f)
                    .padding(16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(
                    if (selectedCategory == null) songs
                    else songs.filter { it.songCategories.contains(selectedCategory.name) },
                ) {
                    SongCard(song = it, onSongClick = { onSongClick(it) })
                }
            }
        }
    }
}