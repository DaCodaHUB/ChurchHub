package com.dangle.churchhub.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.dangle.churchhub.data.local.dao.*
import com.dangle.churchhub.data.local.entity.*

@Database(
    entities = [
        ChurchInfoEntity::class,
        AnnouncementEntity::class,
        ReadingPlanItemEntity::class,
        ReadingPlanCompletedEntity::class,
        YouTubeSermonEntity::class
    ],
    version = 3,
    exportSchema = true
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun churchInfoDao(): ChurchInfoDao
    abstract fun announcementDao(): AnnouncementDao
    abstract fun readingPlanDao(): ReadingPlanDao
    abstract fun readingPlanCompletionDao(): ReadingPlanCompletionDao
    abstract fun youTubeSermonDao(): YouTubeSermonDao
}
