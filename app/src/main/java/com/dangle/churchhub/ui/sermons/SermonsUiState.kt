package com.dangle.churchhub.ui.sermons

import com.dangle.churchhub.domain.model.Sermon

data class SermonsUiState(
    val query: String = "",
    val isRefreshing: Boolean = false,
    val error: String? = null,
    val items: List<Sermon> = emptyList()
)
