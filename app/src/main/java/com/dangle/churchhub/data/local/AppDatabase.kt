package com.dangle.churchhub.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.dangle.churchhub.data.local.dao.AnnouncementDao
import com.dangle.churchhub.data.local.entity.AnnouncementEntity

@Database(
    entities = [AnnouncementEntity::class],
    version = 1,
    exportSchema = true
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun announcementDao(): AnnouncementDao
}