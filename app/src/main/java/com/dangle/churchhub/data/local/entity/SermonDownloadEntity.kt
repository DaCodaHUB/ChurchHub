package com.dangle.churchhub.data.local.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "sermon_downloads",
    indices = [Index(value = ["downloadId"], unique = true)]
)
data class SermonDownloadEntity(
    @PrimaryKey val sermonId: String,
    val state: String,         // NOT_DOWNLOADED / QUEUED / DOWNLOADED / FAILED
    val downloadId: Long?,     // DownloadManager id
    val localUri: String?,     // content:// or file:// string
    val lastError: String?,
    val updatedAtEpochMs: Long
)
