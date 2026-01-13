package com.dangle.churchhub.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "sermons")
data class SermonEntity(
    @PrimaryKey val id: String,
    val title: String,
    val speaker: String,
    val series: String?,
    val dateEpochMs: Long,
    val audioUrl: String,
    val durationSec: Int?,
    val notesMarkdown: String?,
    val updatedAtEpochMs: Long
)
