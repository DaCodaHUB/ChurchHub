package com.dangle.churchhub.core.di

import android.app.DownloadManager
import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object SystemModule {
    @Provides @Singleton
    fun provideDownloadManager(@ApplicationContext context: Context): DownloadManager =
        context.getSystemService(DownloadManager::class.java)
}
