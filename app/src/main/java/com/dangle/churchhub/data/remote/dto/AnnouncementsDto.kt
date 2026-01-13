package com.dangle.churchhub.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class AnnouncementsResponseDto(
    val version: Int,
    val generatedAt: String,
    val announcements: List<AnnouncementDto>
)

@Serializable
data class AnnouncementDto(
    val id: String,
    val title: String,
    val bodyMarkdown: String,
    val category: String,
    val pinned: Boolean,
    val publishedAt: String,
    val updatedAt: String
)
