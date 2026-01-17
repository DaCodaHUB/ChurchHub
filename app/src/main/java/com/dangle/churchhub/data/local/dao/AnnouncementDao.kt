package com.dangle.churchhub.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.dangle.churchhub.data.local.entity.AnnouncementEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface AnnouncementDao {

    @Query("SELECT * FROM announcements_bulletin WHERE id = 1 LIMIT 1")
    fun observe(): Flow<AnnouncementEntity?>

    @Upsert
    suspend fun upsert(entity: AnnouncementEntity)
}
