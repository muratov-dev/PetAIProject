package me.yeahapps.mypetai.feature.discover.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import me.yeahapps.mypetai.feature.discover.data.repository.DiscoverRepositoryImpl
import me.yeahapps.mypetai.feature.discover.domain.repository.DiscoverRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class DiscoverRepositoryModule {

    @Singleton
    @Binds
    abstract fun bindDiaryRepository(repository: DiscoverRepositoryImpl): DiscoverRepository
}
