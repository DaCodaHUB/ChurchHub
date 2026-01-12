package com.dangle.churchhub.data.mappers

import com.dangle.churchhub.data.local.entity.AnnouncementEntity
import com.dangle.churchhub.data.remote.dto.AnnouncementDto
import java.time.Instant

fun AnnouncementDto.toEntity(): AnnouncementEntity {
    val published = Instant.parse(publishedAt).toEpochMilli()
    val updated = Instant.parse(updatedAt).toEpochMilli()
    return AnnouncementEntity(
        id = id,
        title = title,
        bodyMarkdown = bodyMarkdown,
        category = category,
        pinned = pinned,
        publishedAtEpochMs = published,
        updatedAtEpochMs = updated
    )
}