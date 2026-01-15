package com.dangle.churchhub.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class AnnouncementsResponseDto(
    val version: Int? = null,
    val generatedAt: String? = null,
    val announcements: List<AnnouncementDto>
)

@Serializable
data class AnnouncementDto(
    val id: String,
    val title: String,
    val category: String,
    val pinned: Boolean,
    val publishedAt: String,
    val updatedAt: String,

    // Simple announcement
    val bodyMarkdown: String? = null,

    // Bulletin-style announcement
    val format: String? = null,          // "bulletin"
    val bulletin: BulletinDto? = null,

    // optional bilingual title in your sample
    val title_en: String? = null
)

@Serializable
data class BulletinDto(
    val sections: List<BulletinSectionDto>
)

@Serializable
data class BulletinSectionDto(
    val heading: String,
    val headingEn: String? = null,
    val items: List<BulletinItemDto>
)

@Serializable
data class BulletinItemDto(
    val text: String,
    val details: String? = null,
    val subitems: List<BulletinSubItemDto>? = null,
    val meta: List<BulletinMetaDto>? = null
)

@Serializable
data class BulletinSubItemDto(
    val label: String,
    val text: String,
    val meta: List<BulletinMetaDto>? = null
)

@Serializable
data class BulletinMetaDto(
    val label: String,
    val value: String
)
