package com.dangle.churchhub.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AnnouncementsDto(
    val version: Int = 1,
    @SerialName("generated_at")
    val generatedAt: String = "",
    val announcements: List<AnnouncementSectionDto> = emptyList()
)

@Serializable
data class AnnouncementSectionDto(
    @SerialName("heading_en")
    val headingEn: String = "",

    @SerialName("heading_vi")
    val headingVi: String = "",

    val items: List<AnnouncementItemDto> = emptyList()
)

@Serializable
data class AnnouncementItemDto(
    @SerialName("text_en")
    val textEn: String = "",

    @SerialName("text_vi")
    val textVi: String = "",

    @SerialName("details_en")
    val detailsEn: String? = null,

    @SerialName("details_vi")
    val detailsVi: String? = null,

    val meta: List<AnnouncementMetaDto>? = null,
    val subitems: List<AnnouncementSubItemDto>? = null
)

@Serializable
data class AnnouncementMetaDto(
    @SerialName("label_en")
    val labelEn: String = "",

    @SerialName("label_vi")
    val labelVi: String = "",

    val value: String = ""
)

@Serializable
data class AnnouncementSubItemDto(
    @SerialName("label_en")
    val labelEn: String = "",

    @SerialName("label_vi")
    val labelVi: String = "",

    @SerialName("text_en")
    val textEn: String = "",

    @SerialName("text_vi")
    val textVi: String = ""
)
