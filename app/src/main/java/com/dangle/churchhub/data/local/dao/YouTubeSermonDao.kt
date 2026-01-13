package com.dangle.churchhub.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.dangle.churchhub.data.local.entity.YouTubeSermonEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface YouTubeSermonDao {
    @Query("SELECT * FROM youtube_sermons ORDER BY dateEpochMs DESC")
    fun observeAll(): Flow<List<YouTubeSermonEntity>>

    @Upsert
    suspend fun upsertAll(items: List<YouTubeSermonEntity>)

    @Query("DELETE FROM youtube_sermons")
    suspend fun clear()
}
