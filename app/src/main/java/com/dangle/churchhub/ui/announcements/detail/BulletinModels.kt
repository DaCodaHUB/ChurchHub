package com.dangle.churchhub.ui.announcements.detail

import kotlinx.serialization.Serializable

@Serializable
data class BulletinDto(
    val sections: List<BulletinSectionDto> = emptyList()
)

@Serializable
data class BulletinSectionDto(
    val heading: String,
    val headingEn: String? = null,
    val items: List<BulletinItemDto> = emptyList()
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
