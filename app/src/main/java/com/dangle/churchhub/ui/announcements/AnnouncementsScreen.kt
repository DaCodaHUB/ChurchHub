package com.dangle.churchhub.ui.announcements

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.dangle.churchhub.data.local.entity.AnnouncementEntity

@Composable
fun AnnouncementsScreen(
    onOpenAnnouncement: (String) -> Unit,
    vm: AnnouncementsViewModel = hiltViewModel()
) {
    val state by vm.uiState.collectAsState()

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Header
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text("Announcements", style = MaterialTheme.typography.headlineSmall)
            TextButton(onClick = vm::refresh, enabled = !state.isRefreshing) {
                Text(if (state.isRefreshing) "Refreshing…" else "Refresh")
            }
        }

        // Error banner
        state.error?.let { msg ->
            Text(
                text = "Error: $msg",
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodyMedium
            )
        }

        // Category chips (optional)
        AnnouncementsCategoryChips(
            categories = state.categories,
            selectedCategory = state.selectedCategory,
            onSelect = vm::selectCategory
        )

        Spacer(Modifier.height(4.dp))

        // List
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(state.items, key = { it.id }) { a ->
                AnnouncementCard(
                    a = a,
                    onClick = { onOpenAnnouncement(a.id) }
                )
            }
        }
    }
}

@Composable
private fun AnnouncementsCategoryChips(
    categories: List<String>,
    selectedCategory: String?,
    onSelect: (String?) -> Unit
) {
    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        FilterChip(
            selected = selectedCategory == null,
            onClick = { onSelect(null) },
            label = { Text("All") }
        )
        categories.forEach { cat ->
            FilterChip(
                selected = selectedCategory == cat,
                onClick = { onSelect(cat) },
                label = { Text(cat) }
            )
        }
    }
}

@Composable
private fun AnnouncementCard(
    a: AnnouncementEntity,
    onClick: () -> Unit
) {
    Card(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(a.title, style = MaterialTheme.typography.titleMedium)

                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    if (a.pinned) {
                        Text("PINNED", style = MaterialTheme.typography.labelSmall)
                    }
                    if (a.format == "bulletin") {
                        Text("BULLETIN", style = MaterialTheme.typography.labelSmall)
                    }
                }
            }

            Text(a.category, style = MaterialTheme.typography.labelMedium)

            val preview = when {
                a.format == "bulletin" -> "Tap to view bulletin"
                !a.bodyMarkdown.isNullOrBlank() -> a.bodyMarkdown!!
                else -> ""
            }

            if (preview.isNotBlank()) {
                Text(preview, maxLines = 3, style = MaterialTheme.typography.bodyMedium)
            }
        }
    }
}
