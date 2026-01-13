package com.dangle.churchhub.domain.model

import java.time.Instant

data class Sermon(
    val id: String,
    val title: String,
    val speaker: String,
    val series: String?,
    val date: Instant,
    val audioUrl: String,
    val durationSec: Int?,
    val notesMarkdown: String?,
    val updatedAt: Instant
)
