package dev.ymuratov.petai.feature.discover.ui.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import dev.ymuratov.petai.R
import dev.ymuratov.petai.core.ui.theme.PetAITheme
import dev.ymuratov.petai.feature.discover.domain.model.SongCategoryModel
import dev.ymuratov.petai.feature.discover.domain.model.SongModel

@Composable
fun CategoryItem(
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
                style = PetAITheme.typography.headlineSemiBold,
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