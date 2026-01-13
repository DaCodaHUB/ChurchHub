package com.dangle.churchhub.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "reading_plan_items")
data class ReadingPlanItemEntity(
    @PrimaryKey val id: String,
    val dateEpochMs: Long,
    val dayNumber: Int,
    val reading: String,
    val psalm: String?,
    val planTitle: String?,
    val updatedAtEpochMs: Long
)
