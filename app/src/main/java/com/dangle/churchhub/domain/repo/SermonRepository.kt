package com.dangle.churchhub.domain.repo

import com.dangle.churchhub.domain.model.Sermon
import kotlinx.coroutines.flow.Flow

interface SermonRepository {
    fun observeSermons(query: String): Flow<List<Sermon>>
    fun observeSermon(id: String): Flow<Sermon?>
    suspend fun syncSermons(): Result<Unit>
}
