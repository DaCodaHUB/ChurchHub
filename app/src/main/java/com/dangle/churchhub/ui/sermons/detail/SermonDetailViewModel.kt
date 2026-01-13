package com.dangle.churchhub.ui.sermons.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dangle.churchhub.data.player.PlayerManager
import com.dangle.churchhub.domain.model.DownloadState
import com.dangle.churchhub.domain.model.PlayerState
import com.dangle.churchhub.domain.repo.DownloadRepository
import com.dangle.churchhub.domain.repo.SermonRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SermonDetailViewModel @Inject constructor(
    private val repo: SermonRepository,
    private val playerManager: PlayerManager,
    private val downloads: DownloadRepository
) : ViewModel() {

    private val _sermonState = MutableStateFlow(SermonDetailUiState())
    private var latestDownloadState: DownloadState = DownloadState.NotDownloaded
    val uiState: StateFlow<SermonDetailUiState> = _sermonState

    fun load(sermonId: String) {
        viewModelScope.launch {
            // Combine sermon info + player state into one UI state
            repo.observeSermon(sermonId)
                .combine(downloads.observeState(sermonId)) { sermon, dl ->
                    latestDownloadState = dl
                    sermon
                }
                .combine(playerManager.state) { sermon, player ->
                    if (sermon == null) {
                        SermonDetailUiState(isLoading = false, player = player)
                    } else {
                        SermonDetailUiState(
                            isLoading = false,
                            sermonTitle = sermon.title,
                            speakerSeries = listOfNotNull(sermon.speaker, sermon.series).joinToString(" • "),
                            dateText = sermon.date.toString(), // we’ll pretty-format in UI or here later
                            notes = sermon.notesMarkdown,
                            audioUrl = sermon.audioUrl,
                            player = player
                        )
                    }
            }.collect { combined ->
                _sermonState.value = combined
            }
        }
    }

    fun onPlayPauseClicked(sermonId: String) {
        val remoteUrl = _sermonState.value.audioUrl ?: return

        val resolvedSource: String = when (val dl = latestDownloadState) {
            is DownloadState.Downloaded -> dl.localUri
            else -> remoteUrl
        }

        playerManager.toggle(
            mediaId = sermonId,
            source = resolvedSource
        )
    }

    fun onSeek(positionMs: Long) {
        playerManager.seekTo(positionMs)
    }

    fun onDownloadClicked(sermonId: String) {
        val url = _sermonState.value.audioUrl ?: return
        viewModelScope.launch { downloads.enqueue(sermonId, url) }
    }
}
