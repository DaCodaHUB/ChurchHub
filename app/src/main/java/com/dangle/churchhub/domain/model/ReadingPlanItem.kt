package com.dangle.churchhub.domain.model

data class ReadingPlanItem(
    val id: String,
    val dateEpochMs: Long,
    val dayNumber: Int,
    val reading: String,
    val psalm: String?,
    val isCompleted: Boolean
)
