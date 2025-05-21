package me.yeahapps.mypetai.feature.profile.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import me.yeahapps.mypetai.feature.profile.data.repository.MyWorksRepositoryImpl
import me.yeahapps.mypetai.feature.profile.domain.repository.MyWorksRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class MyWorksRepositoryModule {

    @Singleton
    @Binds
    abstract fun bindMyWorksRepository(repository: MyWorksRepositoryImpl): MyWorksRepository
}
