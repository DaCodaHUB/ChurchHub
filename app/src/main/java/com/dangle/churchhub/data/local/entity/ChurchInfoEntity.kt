package com.dangle.churchhub.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "church_info")
data class ChurchInfoEntity(
    @PrimaryKey val id: Int = 1,
    val name: String,
    val tagline: String?,
    val addressLine1: String,
    val city: String,
    val state: String,
    val zip: String,
    val lat: Double?,
    val lng: Double?,
    val phone: String?,
    val email: String?,
    val website: String?,
    val giving: String?,
    val youtubeChannel: String?,
    val instagram: String?,
    val updatedAtEpochMs: Long
)
