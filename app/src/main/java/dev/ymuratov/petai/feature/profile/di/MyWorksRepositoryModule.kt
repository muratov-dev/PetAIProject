package dev.ymuratov.petai.feature.profile.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dev.ymuratov.petai.feature.profile.data.repository.MyWorksRepositoryImpl
import dev.ymuratov.petai.feature.profile.domain.repository.MyWorksRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class MyWorksRepositoryModule {

    @Singleton
    @Binds
    abstract fun bindMyWorksRepository(repository: MyWorksRepositoryImpl): MyWorksRepository
}
