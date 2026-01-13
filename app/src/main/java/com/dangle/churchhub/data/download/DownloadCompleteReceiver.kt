package com.dangle.churchhub.data.download

import android.app.DownloadManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.Uri
import com.dangle.churchhub.data.local.dao.SermonDownloadDao
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File
import java.time.Instant
import javax.inject.Inject

@AndroidEntryPoint
class DownloadCompleteReceiver : BroadcastReceiver() {

    @Inject lateinit var dm: DownloadManager
    @Inject lateinit var downloadDao: SermonDownloadDao

    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action != DownloadManager.ACTION_DOWNLOAD_COMPLETE) return
        val downloadId = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1L)
        if (downloadId <= 0) return

        CoroutineScope(Dispatchers.IO).launch {
            val row = downloadDao.getByDownloadId(downloadId) ?: return@launch

            val query = DownloadManager.Query().setFilterById(downloadId)
            dm.query(query).use { cursor ->
                if (cursor == null || !cursor.moveToFirst()) return@launch

                val status = cursor.getInt(cursor.getColumnIndexOrThrow(DownloadManager.COLUMN_STATUS))
                val localUriStr = cursor.getString(cursor.getColumnIndexOrThrow(DownloadManager.COLUMN_LOCAL_URI))
                val reason = cursor.getInt(cursor.getColumnIndexOrThrow(DownloadManager.COLUMN_REASON))

                if (status == DownloadManager.STATUS_SUCCESSFUL) {
                    val path = Uri.parse(localUriStr).path
                    val ok = path != null && File(path).exists()

                    downloadDao.upsert(
                        row.copy(
                            state = if (ok) "DOWNLOADED" else "FAILED",
                            localUri = if (ok) localUriStr else row.localUri,
                            lastError = if (ok) null else "File missing after download",
                            updatedAtEpochMs = Instant.now().toEpochMilli()
                        )
                    )
                } else {
                    downloadDao.upsert(
                        row.copy(
                            state = "FAILED",
                            lastError = "Download failed (reason=$reason)",
                            updatedAtEpochMs = Instant.now().toEpochMilli()
                        )
                    )
                }
            }
        }
    }
}
