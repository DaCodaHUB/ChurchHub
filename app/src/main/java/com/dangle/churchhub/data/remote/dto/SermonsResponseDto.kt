package com.dangle.churchhub.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class SermonsResponseDto(
    val version: Int,
    val generatedAt: String,
    val sermons: List<SermonDto>
)

@Serializable
data class SermonDto(
    val id: String,
    val title: String,
    val speaker: String,
    val series: String? = null,
    val date: String,
    val audioUrl: String,
    val durationSec: Int? = null,
    val notesMarkdown: String? = null,
    val updatedAt: String
)
