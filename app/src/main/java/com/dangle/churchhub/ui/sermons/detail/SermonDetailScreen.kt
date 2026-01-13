package com.dangle.churchhub.ui.sermons.detail

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.DividerDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import kotlinx.coroutines.delay
import kotlin.math.max

/**
 * Assumes your SermonDetailViewModel exposes:
 * - val uiState: StateFlow<SermonDetailUiState>
 * - fun load(sermonId: String)
 * - fun onPlayPauseClicked(sermonId: String)
 * - fun onSeek(positionMs: Long)
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SermonDetailScreen(
    sermonId: String,
    onBack: () -> Unit,
    vm: SermonDetailViewModel = hiltViewModel()
) {
    val state by vm.uiState.collectAsState()

    // Load sermon + start observing player state (in your VM)
    LaunchedEffect(sermonId) { vm.load(sermonId) }

    // ---- Scrubber + ticker state (UI-only) ----
    // We keep a local "displayed position" that ticks while playing,
    // and snaps back to real player updates whenever they change.
    var isUserScrubbing by remember { mutableStateOf(false) }
    var scrubPositionMs by remember { mutableLongStateOf(0L) }
    var displayedPositionMs by remember { mutableLongStateOf(0L) }

    val durationMs = max(state.player.durationMs, 0L)
    val canScrub = durationMs > 0L

    // Snap displayed position whenever the player reports a new position (e.g., seek, buffer updates)
    LaunchedEffect(state.player.positionMs, isUserScrubbing) {
        if (!isUserScrubbing) {
            displayedPositionMs = state.player.positionMs.coerceIn(0L, if (canScrub) durationMs else Long.MAX_VALUE)
        }
    }

    // Ticker: while playing, advance displayedPositionMs every 500ms (approximate UI ticker)
    LaunchedEffect(state.player.isPlaying, durationMs, isUserScrubbing) {
        while (state.player.isPlaying && !isUserScrubbing) {
            delay(500)
            if (canScrub) {
                displayedPositionMs = (displayedPositionMs + 500L).coerceIn(0L, durationMs)
            } else {
                displayedPositionMs += 500L
            }
        }
    }

    // Use scrubPosition during scrubbing; otherwise use displayed ticker position
    val effectivePositionMs = if (isUserScrubbing) scrubPositionMs else displayedPositionMs

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Sermon") },
                navigationIcon = {
                    TextButton(onClick = onBack) { Text("Back") }
                }
            )
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                if (state.isLoading) {
                    Text("Loading…", style = MaterialTheme.typography.titleMedium)
                    return@Column
                }

                if (state.audioUrl == null) {
                    Text("Sermon not found.", style = MaterialTheme.typography.titleMedium)
                    return@Column
                }

                // Header
                Text(state.sermonTitle, style = MaterialTheme.typography.headlineSmall)
                if (state.speakerSeries.isNotBlank()) {
                    Text(state.speakerSeries, style = MaterialTheme.typography.titleSmall)
                }
                if (state.dateText.isNotBlank()) {
                    Text(state.dateText, style = MaterialTheme.typography.labelLarge)
                }

                HorizontalDivider(Modifier, DividerDefaults.Thickness, DividerDefaults.color)

                // Player controls row
                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    Button(onClick = { vm.onPlayPauseClicked(sermonId) }) {
                        Text(if (state.player.isPlaying) "Pause" else "Play")
                    }
                    OutlinedButton(onClick = { vm.onDownloadClicked(sermonId) }) { Text("Download") }

                }

                // Player detail + scrubber
                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    val left = formatTime(effectivePositionMs)
                    val right = if (durationMs > 0) formatTime(durationMs) else "--:--"

                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text(left, style = MaterialTheme.typography.bodySmall)
                        Text(right, style = MaterialTheme.typography.bodySmall)
                    }

                    Slider(
                        value = if (canScrub) effectivePositionMs.toFloat() else 0f,
                        onValueChange = { newValue ->
                            if (!canScrub) return@Slider
                            isUserScrubbing = true
                            scrubPositionMs = newValue.toLong().coerceIn(0L, durationMs)
                        },
                        onValueChangeFinished = {
                            if (!canScrub) return@Slider
                            vm.onSeek(scrubPositionMs)
                            isUserScrubbing = false
                        },
                        valueRange = if (canScrub) 0f..durationMs.toFloat() else 0f..1f,
                        enabled = canScrub
                    )

                    Text(
                        text = "State: ${if (state.player.isPlaying) "Playing" else "Paused"}",
                        style = MaterialTheme.typography.bodySmall
                    )
                }

                HorizontalDivider(Modifier, DividerDefaults.Thickness, DividerDefaults.color)

                // Notes
                Text("Notes", style = MaterialTheme.typography.titleMedium)
                if (!state.notes.isNullOrBlank()) {
                    Text(state.notes!!, style = MaterialTheme.typography.bodyMedium)
                } else {
                    Text("No notes available.", style = MaterialTheme.typography.bodyMedium)
                }

                Spacer(Modifier.height(24.dp))
            }
        }
    }
}

private fun formatTime(ms: Long): String {
    val totalSec = (ms.coerceAtLeast(0L) / 1000L).toInt()
    val m = totalSec / 60
    val s = totalSec % 60
    return "%d:%02d".format(m, s)
}
