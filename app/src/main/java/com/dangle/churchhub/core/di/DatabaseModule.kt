package com.dangle.churchhub.core.di

import android.content.Context
import androidx.room.Room
import com.dangle.churchhub.data.local.AppDatabase
import com.dangle.churchhub.data.local.dao.AnnouncementDao
import com.dangle.churchhub.data.local.dao.SermonDao
import com.dangle.churchhub.data.local.dao.SermonDownloadDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides @Singleton
    fun provideDb(@ApplicationContext context: Context): AppDatabase =
        Room.databaseBuilder(context, AppDatabase::class.java, "churchhub.db")
            .fallbackToDestructiveMigration()
            .build()

    @Provides
    fun provideAnnouncementDao(db: AppDatabase): AnnouncementDao = db.announcementDao()

    @Provides
    fun provideSermonDao(db: AppDatabase): SermonDao = db.sermonDao()

    @Provides
    fun provideSermonDownloadDao(db: AppDatabase): SermonDownloadDao = db.sermonDownloadDao()
}