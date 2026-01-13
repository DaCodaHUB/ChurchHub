package com.dangle.churchhub.core.di

import android.content.Context
import androidx.room.Room
import com.dangle.churchhub.data.local.AppDatabase
import com.dangle.churchhub.data.local.dao.*
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

    @Provides fun provideChurchInfoDao(db: AppDatabase): ChurchInfoDao = db.churchInfoDao()
    @Provides fun provideAnnouncementDao(db: AppDatabase): AnnouncementDao = db.announcementDao()
    @Provides fun provideReadingPlanDao(db: AppDatabase): ReadingPlanDao = db.readingPlanDao()
    @Provides fun provideReadingPlanCompletionDao(db: AppDatabase): ReadingPlanCompletionDao = db.readingPlanCompletionDao()
    @Provides fun provideYouTubeSermonDao(db: AppDatabase): YouTubeSermonDao = db.youTubeSermonDao()
}
