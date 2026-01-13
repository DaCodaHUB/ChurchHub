package com.dangle.churchhub.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class ReadingPlanResponseDto(
    val version: Int,
    val generatedAt: String,
    val planTitle: String? = null,
    val items: List<ReadingPlanItemDto>
)

@Serializable
data class ReadingPlanItemDto(
    val id: String,
    val date: String,      // "YYYY-MM-DD"
    val dayNumber: Int,
    val reading: String,
    val psalm: String? = null
)
