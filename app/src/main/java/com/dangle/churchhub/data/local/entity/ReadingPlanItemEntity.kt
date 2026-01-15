package com.dangle.churchhub.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "reading_plan_items")
data class ReadingPlanItemEntity(
    @PrimaryKey val id: String,          // use date, e.g. "2026-01-13"
    val dateEpochMs: Long,
    val dayNumber: Int,

    val displayDate: String,
    val displayDateVi: String,

    val reading: String,
    val readingVi: String,

    val psalm: String,
    val psalmVi: String,

    val planTitle: String,
    val planTitleVi: String,

    val updatedAtEpochMs: Long
)
