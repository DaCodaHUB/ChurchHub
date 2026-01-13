package com.dangle.churchhub.data.repository

import com.dangle.churchhub.core.util.parseLocalDateToEpochMs
import com.dangle.churchhub.data.local.dao.YouTubeSermonDao
import com.dangle.churchhub.data.local.entity.YouTubeSermonEntity
import com.dangle.churchhub.data.remote.api.SermonsYouTubeApi
import com.dangle.churchhub.domain.repo.SermonsRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SermonsRepositoryImpl @Inject constructor(
    private val api: SermonsYouTubeApi,
    private val dao: YouTubeSermonDao
) : SermonsRepository {

    override fun observeSermons(): Flow<List<YouTubeSermonEntity>> = dao.observeAll()

    override suspend fun refresh(): Result<Unit> = runCatching {
        val dto = api.fetchYouTubeSermons()
        val now = System.currentTimeMillis()

        val entities = dto.videos.map { v ->
            YouTubeSermonEntity(
                id = v.id,
                title = v.title,
                dateEpochMs = parseLocalDateToEpochMs(v.date),
                speaker = v.speaker,
                series = v.series,
                youtubeVideoId = v.youtubeVideoId,
                updatedAtEpochMs = now
            )
        }

        dao.clear()
        dao.upsertAll(entities)
    }
}
