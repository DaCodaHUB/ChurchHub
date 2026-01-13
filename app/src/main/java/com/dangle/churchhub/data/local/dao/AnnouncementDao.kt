package com.dangle.churchhub.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.dangle.churchhub.data.local.entity.AnnouncementEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface AnnouncementDao {
    @Query("SELECT * FROM announcements ORDER BY pinned DESC, publishedAtEpochMs DESC")
    fun observeAll(): Flow<List<AnnouncementEntity>>

    @Upsert
    suspend fun upsertAll(items: List<AnnouncementEntity>)

    @Query("DELETE FROM announcements")
    suspend fun clear()
}
