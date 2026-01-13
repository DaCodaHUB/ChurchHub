package com.dangle.churchhub.data.repository

import com.dangle.churchhub.data.local.dao.SermonDao
import com.dangle.churchhub.data.mappers.toEntity
import com.dangle.churchhub.data.remote.api.SermonsApi
import com.dangle.churchhub.domain.model.Sermon
import com.dangle.churchhub.domain.repo.SermonRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.Instant
import javax.inject.Inject

class SermonRepositoryImpl @Inject constructor(
    private val api: SermonsApi,
    private val dao: SermonDao
) : SermonRepository {

    override fun observeSermons(query: String): Flow<List<Sermon>> {
        val flow = if (query.isBlank()) dao.observeAll() else dao.observeSearch(query.trim())
        return flow.map { entities ->
            entities.map { e ->
                Sermon(
                    id = e.id,
                    title = e.title,
                    speaker = e.speaker,
                    series = e.series,
                    date = Instant.ofEpochMilli(e.dateEpochMs),
                    audioUrl = e.audioUrl,
                    durationSec = e.durationSec,
                    notesMarkdown = e.notesMarkdown,
                    updatedAt = Instant.ofEpochMilli(e.updatedAtEpochMs)
                )
            }
        }
    }

    override fun observeSermon(id: String): Flow<Sermon?> =
        dao.observeById(id).map { e ->
            e?.let {
                Sermon(
                    id = it.id,
                    title = it.title,
                    speaker = it.speaker,
                    series = it.series,
                    date = Instant.ofEpochMilli(it.dateEpochMs),
                    audioUrl = it.audioUrl,
                    durationSec = it.durationSec,
                    notesMarkdown = it.notesMarkdown,
                    updatedAt = Instant.ofEpochMilli(it.updatedAtEpochMs)
                )
            }
        }

    override suspend fun syncSermons(): Result<Unit> = runCatching {
        val dto = api.fetchSermons()
        val entities = dto.sermons.map { it.toEntity() }
        dao.upsertAll(entities)
    }
}
