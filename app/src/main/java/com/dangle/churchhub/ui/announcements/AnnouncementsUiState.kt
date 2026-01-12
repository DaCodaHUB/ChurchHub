package com.dangle.churchhub.ui.announcements

import com.dangle.churchhub.domain.model.Announcement

data class AnnouncementsUiState(
    val isRefreshing: Boolean = false,
    val error: String? = null,
    val items: List<Announcement> = emptyList()
)