package com.dangle.churchhub.core.di

import com.dangle.churchhub.data.remote.api.*
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    private const val BASE_URL = "https://raw.githubusercontent.com/DaCodaHUB/ChurchHub/main/"

    @Provides @Singleton
    fun provideJson(): Json = Json {
        ignoreUnknownKeys = true
        isLenient = true
    }

    @Provides @Singleton
    fun provideOkHttp(): OkHttpClient = OkHttpClient.Builder().build()

    @Provides @Singleton
    fun provideRetrofit(okHttp: OkHttpClient, json: Json): Retrofit {
        val contentType = "application/json".toMediaType()
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttp)
            .addConverterFactory(json.asConverterFactory(contentType))
            .build()
    }

    @Provides @Singleton fun provideChurchInfoApi(retrofit: Retrofit): ChurchInfoApi =
        retrofit.create(ChurchInfoApi::class.java)

    @Provides @Singleton fun provideAnnouncementsApi(retrofit: Retrofit): AnnouncementsApi =
        retrofit.create(AnnouncementsApi::class.java)

    @Provides @Singleton fun provideReadingPlanApi(retrofit: Retrofit): ReadingPlanApi =
        retrofit.create(ReadingPlanApi::class.java)

    @Provides @Singleton fun provideSermonsYouTubeApi(retrofit: Retrofit): SermonsYouTubeApi =
        retrofit.create(SermonsYouTubeApi::class.java)
}
