package com.dangle.churchhub.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "reading_plan_completed")
data class ReadingPlanCompletedEntity(
    @PrimaryKey val itemId: String,
    val completedAtEpochMs: Long
)
