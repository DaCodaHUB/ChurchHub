package com.dangle.churchhub.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "youtube_sermons")
data class YouTubeSermonEntity(
    @PrimaryKey val id: String,
    val title: String,
    val dateEpochMs: Long,
    val speaker: String?,
    val series: String?,
    val youtubeVideoId: String,
    val updatedAtEpochMs: Long
) {
    val thumbnailUrl: String
        get() = "https://img.youtube.com/vi/$youtubeVideoId/hqdefault.jpg"
}
