package com.dangle.churchhub.domain.model

import java.time.Instant

data class Announcement(
    val id: String,
    val title: String,
    val bodyMarkdown: String,
    val category: String,
    val pinned: Boolean,
    val publishedAt: Instant,
    val updatedAt: Instant
)