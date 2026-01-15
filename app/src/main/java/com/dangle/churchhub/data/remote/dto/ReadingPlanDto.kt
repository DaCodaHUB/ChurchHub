package com.dangle.churchhub.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ReadingPlanResponseDto(
    val planTitle: String,
    @SerialName("planTitle_vi") val planTitleVi: String,
    val items: List<ReadingPlanItemDto>
)

@Serializable
data class ReadingPlanItemDto(
    val dayNumber: Int,
    val date: String, // "YYYY-MM-DD"
    val displayDate: String,
    @SerialName("displayDate_vi") val displayDateVi: String,
    val reading: String,
    @SerialName("reading_vi") val readingVi: String,
    @SerialName("reading_1") val reading1: String,           // Psalm
    @SerialName("reading_1_vi") val reading1Vi: String       // Psalm (VI)
)
