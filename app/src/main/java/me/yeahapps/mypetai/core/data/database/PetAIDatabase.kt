package me.yeahapps.mypetai.core.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import me.yeahapps.mypetai.core.data.database.converter.IntListTypeConverter
import me.yeahapps.mypetai.core.data.database.converter.StringListTypeConverter
import me.yeahapps.mypetai.feature.discover.data.local.DiscoverDao
import me.yeahapps.mypetai.feature.discover.data.local.VideoTypeConverters
import me.yeahapps.mypetai.feature.discover.data.model.entity.SongCategoryEntity
import me.yeahapps.mypetai.feature.discover.data.model.entity.SongEntity
import me.yeahapps.mypetai.feature.profile.data.local.MyWorksDao
import me.yeahapps.mypetai.feature.profile.data.model.MyWorkEntity

@Database(
    entities = [SongCategoryEntity::class, SongEntity::class, MyWorkEntity::class], version = 2, exportSchema = false
)
@TypeConverters(
    IntListTypeConverter::class,
    StringListTypeConverter::class,
    VideoTypeConverters::class,
)
abstract class PetAIDatabase : RoomDatabase() {
    abstract val discoverDao: DiscoverDao
    abstract val myWorksDao: MyWorksDao
}