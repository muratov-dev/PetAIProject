package dev.ymuratov.petai.core.data.database.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import dev.ymuratov.petai.core.data.database.PetAIDatabase
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Singleton
    @Provides
    fun provideMeditoriumDatabase(@ApplicationContext context: Context): PetAIDatabase =
        Room.databaseBuilder(context, PetAIDatabase::class.java, "petAI.db").fallbackToDestructiveMigration().build()

    @Provides
    fun provideDiscoverDao(database: PetAIDatabase) = database.discoverDao

    @Provides
    fun provideMyWorksDao(database: PetAIDatabase) = database.myWorksDao
}