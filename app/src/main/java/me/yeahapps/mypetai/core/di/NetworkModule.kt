package me.yeahapps.mypetai.core.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import me.yeahapps.mypetai.BuildConfig
import me.yeahapps.mypetai.core.data.network.NetworkErrorInterceptor
import me.yeahapps.mypetai.core.data.network.api.MainApiService
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import okhttp3.logging.HttpLoggingInterceptor.Level
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideLoggingInterceptor(): HttpLoggingInterceptor =
        HttpLoggingInterceptor().setLevel(if (BuildConfig.DEBUG) Level.BODY else Level.NONE)

    @Provides
    @Singleton
    fun provideNetworkInterceptor(): NetworkErrorInterceptor = NetworkErrorInterceptor()

    @Provides
    @Singleton
    fun provideClient(
        httpLoggingInterceptor: HttpLoggingInterceptor, networkErrorInterceptor: NetworkErrorInterceptor
    ) = OkHttpClient.Builder().addInterceptor(httpLoggingInterceptor).addInterceptor(networkErrorInterceptor)
        .connectTimeout(30, TimeUnit.SECONDS).writeTimeout(30, TimeUnit.SECONDS).readTimeout(30, TimeUnit.SECONDS)
        .build()

    @Provides
    @Singleton
    fun provideRetrofit(httpClient: OkHttpClient, json: Json): Retrofit {
        val contentType = "application/json".toMediaType()
        return Retrofit.Builder().baseUrl("https://dreamfaceapp.com/df-server/").client(httpClient)
            .addConverterFactory(json.asConverterFactory(contentType)).build()
    }

    @Provides
    @Singleton
    fun provideApiService(retrofit: Retrofit): MainApiService = retrofit.create(MainApiService::class.java)
}
