package com.dangle.churchhub.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "announcements")
data class AnnouncementEntity(
    @PrimaryKey val id: String,
    val title: String,
    val bodyMarkdown: String,
    val category: String,
    val pinned: Boolean,
    val publishedAtEpochMs: Long,
    val updatedAtEpochMs: Long
)
