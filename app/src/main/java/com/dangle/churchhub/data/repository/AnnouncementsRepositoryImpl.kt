package com.dangle.churchhub.data.repository

import com.dangle.churchhub.core.util.parseIsoInstantToEpochMs
import com.dangle.churchhub.data.local.dao.AnnouncementDao
import com.dangle.churchhub.data.local.entity.AnnouncementEntity
import com.dangle.churchhub.data.remote.api.AnnouncementsApi
import com.dangle.churchhub.domain.repo.AnnouncementsRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class AnnouncementsRepositoryImpl @Inject constructor(
    private val api: AnnouncementsApi,
    private val dao: AnnouncementDao
) : AnnouncementsRepository {

    override fun observeAll(): Flow<List<AnnouncementEntity>> = dao.observeAll()

    override suspend fun refresh(): Result<Unit> = runCatching {
        val dto = api.fetchAnnouncements()
        val entities = dto.announcements.map { a ->
            AnnouncementEntity(
                id = a.id,
                title = a.title,
                bodyMarkdown = a.bodyMarkdown,
                category = a.category,
                pinned = a.pinned,
                publishedAtEpochMs = parseIsoInstantToEpochMs(a.publishedAt),
                updatedAtEpochMs = parseIsoInstantToEpochMs(a.updatedAt)
            )
        }
        dao.clear()
        dao.upsertAll(entities)
    }
}
