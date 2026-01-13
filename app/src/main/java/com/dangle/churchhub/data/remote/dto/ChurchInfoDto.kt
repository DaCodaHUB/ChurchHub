package com.dangle.churchhub.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class ChurchInfoResponseDto(
    val version: Int,
    val updatedAt: String,
    val church: ChurchDto
)

@Serializable
data class ChurchDto(
    val name: String,
    val tagline: String? = null,
    val address: AddressDto,
    val serviceTimes: List<ServiceTimeDto> = emptyList(),
    val contact: ContactDto? = null,
    val links: LinksDto? = null
)

@Serializable
data class AddressDto(
    val line1: String,
    val city: String,
    val state: String,
    val zip: String,
    val lat: Double? = null,
    val lng: Double? = null
)

@Serializable
data class ServiceTimeDto(
    val label: String,
    val day: String,
    val time: String
)

@Serializable
data class ContactDto(
    val phone: String? = null,
    val email: String? = null
)

@Serializable
data class LinksDto(
    val website: String? = null,
    val giving: String? = null,
    val youtubeChannel: String? = null,
    val instagram: String? = null
)
