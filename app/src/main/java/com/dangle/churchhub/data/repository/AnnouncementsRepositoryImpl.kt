package com.dangle.churchhub.data.repository

import com.dangle.churchhub.core.util.parseIsoInstantToEpochMs
import com.dangle.churchhub.data.local.dao.AnnouncementDao
import com.dangle.churchhub.data.local.entity.AnnouncementEntity
import com.dangle.churchhub.data.remote.api.AnnouncementsApi
import com.dangle.churchhub.data.remote.dto.BulletinDto
import com.dangle.churchhub.domain.repo.AnnouncementsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.serialization.json.Json
import javax.inject.Inject

class AnnouncementsRepositoryImpl @Inject constructor(
    private val api: AnnouncementsApi,
    private val dao: AnnouncementDao,
    private val json: Json
) : AnnouncementsRepository {

    override fun observeAll(): Flow<List<AnnouncementEntity>> = dao.observeAll()

    override suspend fun refresh(): Result<Unit> = runCatching {
        val dto = api.fetchAnnouncements()
        val entities = dto.announcements.map { a ->
            val bulletinJson = a.bulletin?.let { json.encodeToString(BulletinDto.serializer(), it) }

            AnnouncementEntity(
                id = a.id,
                title = a.title,
                titleEn = a.title_en,
                category = a.category,
                pinned = a.pinned,
                publishedAtEpochMs = parseIsoInstantToEpochMs(a.publishedAt),
                updatedAtEpochMs = parseIsoInstantToEpochMs(a.updatedAt),
                bodyMarkdown = a.bodyMarkdown,
                format = a.format,
                bulletinJson = bulletinJson
            )
        }
        dao.clear()
        dao.upsertAll(entities)
    }
}
