package dev.ymuratov.petai.core.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import dev.ymuratov.petai.feature.discover.data.local.DiscoverDao
import dev.ymuratov.petai.feature.discover.data.local.VideoTypeConverters
import dev.ymuratov.petai.feature.discover.data.model.entity.SongCategoryEntity
import dev.ymuratov.petai.feature.discover.data.model.entity.SongEntity
import online.meditorium.core.data.database.converter.IntListTypeConverter
import online.meditorium.core.data.database.converter.StringListTypeConverter

@Database(entities = [SongCategoryEntity::class, SongEntity::class], version = 1, exportSchema = false)
@TypeConverters(
    IntListTypeConverter::class,
    StringListTypeConverter::class,
    VideoTypeConverters::class,
)
abstract class PetAIDatabase : RoomDatabase() {
    abstract val discoverDao: DiscoverDao
}