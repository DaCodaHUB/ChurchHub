package com.dangle.churchhub.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class SermonsYouTubeResponseDto(
    val version: Int,
    val generatedAt: String,
    val videos: List<YouTubeVideoDto>
)

@Serializable
data class YouTubeVideoDto(
    val id: String,
    val title: String,
    val date: String,              // "YYYY-MM-DD"
    val speaker: String? = null,
    val series: String? = null,
    val youtubeVideoId: String
)
