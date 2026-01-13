package com.dangle.churchhub.domain.model

data class PlayerState(
    val isPlaying: Boolean = false,
    val positionMs: Long = 0L,
    val durationMs: Long = 0L
)
