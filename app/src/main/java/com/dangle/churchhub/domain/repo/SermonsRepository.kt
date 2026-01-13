package com.dangle.churchhub.domain.repo

import com.dangle.churchhub.data.local.entity.YouTubeSermonEntity
import kotlinx.coroutines.flow.Flow

interface SermonsRepository {
    fun observeSermons(): Flow<List<YouTubeSermonEntity>>
    suspend fun refresh(): Result<Unit>
}
