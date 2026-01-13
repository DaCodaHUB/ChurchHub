package com.dangle.churchhub.data.mappers

import com.dangle.churchhub.data.local.entity.SermonEntity
import com.dangle.churchhub.data.remote.dto.SermonDto
import java.time.Instant

fun SermonDto.toEntity(): SermonEntity {
    val dateMs = Instant.parse(date).toEpochMilli()
    val updatedMs = Instant.parse(updatedAt).toEpochMilli()
    return SermonEntity(
        id = id,
        title = title,
        speaker = speaker,
        series = series,
        dateEpochMs = dateMs,
        audioUrl = audioUrl,
        durationSec = durationSec,
        notesMarkdown = notesMarkdown,
        updatedAtEpochMs = updatedMs
    )
}
