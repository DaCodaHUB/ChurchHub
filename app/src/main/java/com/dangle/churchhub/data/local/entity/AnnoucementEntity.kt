package com.dangle.churchhub.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "announcements")
data class AnnouncementEntity(
    @PrimaryKey val id: String,
    val title: String,
    val category: String,
    val pinned: Boolean,
    val publishedAtEpochMs: Long,
    val updatedAtEpochMs: Long,

    // simple type
    val bodyMarkdown: String? = null,

    // bulletin type
    val format: String? = null,
    val bulletinJson: String? = null,

    val titleEn: String? = null
)
