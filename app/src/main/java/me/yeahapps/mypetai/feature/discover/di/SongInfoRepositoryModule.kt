package me.yeahapps.mypetai.feature.discover.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import me.yeahapps.mypetai.feature.discover.data.repository.SongInfoRepositoryImpl
import me.yeahapps.mypetai.feature.discover.domain.repository.SongInfoRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class SongInfoRepositoryModule {

    @Singleton
    @Binds
    abstract fun bindSongInfoRepository(repository: SongInfoRepositoryImpl): SongInfoRepository
}
