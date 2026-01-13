package com.dangle.churchhub.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.dangle.churchhub.data.local.dao.AnnouncementDao
import com.dangle.churchhub.data.local.dao.SermonDao
import com.dangle.churchhub.data.local.dao.SermonDownloadDao
import com.dangle.churchhub.data.local.entity.AnnouncementEntity
import com.dangle.churchhub.data.local.entity.SermonDownloadEntity
import com.dangle.churchhub.data.local.entity.SermonEntity

@Database(
    entities = [
        AnnouncementEntity::class,
        SermonEntity::class,
        SermonDownloadEntity::class
    ],
    version = 2,
    exportSchema = true
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun announcementDao(): AnnouncementDao
    abstract fun sermonDao(): SermonDao
    abstract fun sermonDownloadDao(): SermonDownloadDao
}

