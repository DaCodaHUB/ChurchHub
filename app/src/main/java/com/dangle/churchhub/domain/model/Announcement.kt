package com.dangle.churchhub.domain.model

data class Announcement(
    val id: String,
    val title: String,
    val bodyMarkdown: String,
    val category: String,
    val pinned: Boolean,
    val publishedAtEpochMs: Long
)
