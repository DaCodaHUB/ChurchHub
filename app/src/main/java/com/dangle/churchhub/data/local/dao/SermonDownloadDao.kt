package com.dangle.churchhub.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.dangle.churchhub.data.local.entity.SermonDownloadEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface SermonDownloadDao {

    @Query("SELECT * FROM sermon_downloads WHERE sermonId = :sermonId LIMIT 1")
    fun observeBySermonId(sermonId: String): Flow<SermonDownloadEntity?>

    @Query("SELECT * FROM sermon_downloads WHERE sermonId = :sermonId LIMIT 1")
    suspend fun getBySermonId(sermonId: String): SermonDownloadEntity?

    @Query("SELECT * FROM sermon_downloads WHERE downloadId = :downloadId LIMIT 1")
    suspend fun getByDownloadId(downloadId: Long): SermonDownloadEntity?

    @Upsert
    suspend fun upsert(entity: SermonDownloadEntity)
}
