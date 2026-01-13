package com.dangle.churchhub.domain.model

sealed interface DownloadState {
    data object NotDownloaded : DownloadState
    data object Queued : DownloadState
    data class Downloaded(val localUri: String) : DownloadState
    data class Failed(val message: String) : DownloadState
}
