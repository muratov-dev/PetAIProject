package me.yeahapps.mypetai.feature.create.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import me.yeahapps.mypetai.feature.create.data.repository.CreateRepositoryImpl
import me.yeahapps.mypetai.feature.create.domain.repository.CreateRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class CreateRepositoryModule {

    @Singleton
    @Binds
    abstract fun bindCreateRepository(repository: CreateRepositoryImpl): CreateRepository
}
