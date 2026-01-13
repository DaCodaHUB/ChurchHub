package com.dangle.churchhub.domain.model

data class YouTubeSermon(
    val id: String,
    val title: String,
    val dateEpochMs: Long,
    val speaker: String?,
    val series: String?,
    val youtubeVideoId: String
) {
    val thumbnailUrl: String
        get() = "https://img.youtube.com/vi/$youtubeVideoId/hqdefault.jpg"
}
