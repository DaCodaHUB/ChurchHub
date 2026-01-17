package com.dangle.churchhub.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "announcements_bulletin")
data class AnnouncementEntity(
    @PrimaryKey val id: Int = 1,
    val generatedAt: String,
    val json: String,
    val updatedAtEpochMs: Long
)
