package com.dangle.churchhub.domain.model

data class ChurchInfo(
    val name: String,
    val tagline: String?,
    val address: Address,
    val serviceTimes: List<ServiceTime>,
    val contact: Contact,
    val links: Links
)

data class Address(
    val line1: String,
    val city: String,
    val state: String,
    val zip: String,
    val lat: Double?,
    val lng: Double?
) {
    val formatted: String
        get() = "$line1, $city, $state $zip"
}

data class ServiceTime(
    val label: String,
    val day: String,
    val time: String
)

data class Contact(
    val phone: String?,
    val email: String?
)

data class Links(
    val website: String?,
    val giving: String?,
    val youtubeChannel: String?,
    val instagram: String?
)
