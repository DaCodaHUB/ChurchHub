package com.dangle.churchhub.data.repository

import android.app.DownloadManager
import android.content.Context
import android.net.Uri
import com.dangle.churchhub.data.local.dao.SermonDownloadDao
import com.dangle.churchhub.data.local.entity.SermonDownloadEntity
import com.dangle.churchhub.domain.model.DownloadState
import com.dangle.churchhub.domain.repo.DownloadRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.Instant
import javax.inject.Inject

class DownloadRepositoryImpl @Inject constructor(
    private val dm: DownloadManager,
    private val dao: SermonDownloadDao,
    @ApplicationContext private val context: Context
) : DownloadRepository {

    override fun observeState(sermonId: String): Flow<DownloadState> =
        dao.observeBySermonId(sermonId).map { e ->
            when (e?.state) {
                null, "NOT_DOWNLOADED" -> DownloadState.NotDownloaded
                "QUEUED" -> DownloadState.Queued
                "DOWNLOADED" -> DownloadState.Downloaded(e.localUri ?: "")
                "FAILED" -> DownloadState.Failed(e.lastError ?: "Download failed")
                else -> DownloadState.NotDownloaded
            }
        }

    override suspend fun enqueue(sermonId: String, audioUrl: String): Result<Unit> = runCatching {
        val request = DownloadManager.Request(Uri.parse(audioUrl))
            .setTitle("Sermon")
            .setDescription("Downloading sermon audio")
            .setAllowedOverMetered(true)
            .setAllowedOverRoaming(true)
            .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
            .setDestinationInExternalFilesDir(context, "sermons", "sermon_$sermonId.mp3")

        val downloadId = dm.enqueue(request)

        // Provisional local Uri (we’ll confirm on completion)
        val provisionalUri =
            Uri.fromFile(context.getExternalFilesDir("sermons")!!.resolve("sermon_$sermonId.mp3")).toString()

        dao.upsert(
            SermonDownloadEntity(
                sermonId = sermonId,
                state = "QUEUED",
                downloadId = downloadId,
                localUri = provisionalUri,
                lastError = null,
                updatedAtEpochMs = Instant.now().toEpochMilli()
            )
        )
    }
}
