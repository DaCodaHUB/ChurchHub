package com.dangle.churchhub.domain.repo

import com.dangle.churchhub.domain.model.DownloadState
import kotlinx.coroutines.flow.Flow

interface DownloadRepository {
    fun observeState(sermonId: String): Flow<DownloadState>
    suspend fun enqueue(sermonId: String, audioUrl: String): Result<Unit>
}
