package com.dangle.churchhub.ui.sermons.detail

import com.dangle.churchhub.domain.model.PlayerState

data class SermonDetailUiState(
    val isLoading: Boolean = true,
    val sermonTitle: String = "",
    val speakerSeries: String = "",
    val dateText: String = "",
    val notes: String? = null,
    val audioUrl: String? = null,
    val player: PlayerState = PlayerState()
)
