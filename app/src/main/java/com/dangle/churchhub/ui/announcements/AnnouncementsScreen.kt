package com.dangle.churchhub.ui.announcements

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.dangle.churchhub.data.local.entity.AnnouncementEntity
import com.dangle.churchhub.ui.settings.AppLanguage
import com.dangle.churchhub.ui.settings.SettingsViewModel

@Composable
fun AnnouncementsScreen(
    onOpenAnnouncement: (String) -> Unit,
    vm: AnnouncementsViewModel = hiltViewModel(),
    settingsVm: SettingsViewModel = hiltViewModel()
) {
    val state by vm.uiState.collectAsState()
    val settings by settingsVm.settings.collectAsState()
    val lang = settings.language

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text("Announcements", style = MaterialTheme.typography.headlineSmall)
            TextButton(onClick = vm::refresh, enabled = !state.isRefreshing) {
                Text(if (state.isRefreshing) "Refreshing…" else "Refresh")
            }
        }

        state.error?.let { msg ->
            Text(
                text = "Error: $msg",
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodyMedium
            )
        }

        AnnouncementsCategoryChips(
            categories = state.categories,
            selectedCategory = state.selectedCategory,
            onSelect = vm::selectCategory
        )

        Spacer(Modifier.height(4.dp))

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(state.items, key = { it.id }) { a ->
                AnnouncementCard(
                    a = a,
                    language = lang,
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
    language: AppLanguage,
    onClick: () -> Unit
) {
    val displayTitle =
        if (language == AppLanguage.EN && !a.titleEn.isNullOrBlank()) a.titleEn!!
        else a.title

    Card(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text(displayTitle, style = MaterialTheme.typography.titleMedium)

                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    if (a.pinned) Text("PINNED", style = MaterialTheme.typography.labelSmall)
                    if (a.format == "bulletin") Text("BULLETIN", style = MaterialTheme.typography.labelSmall)
                }
            }

            Text(a.category, style = MaterialTheme.typography.labelMedium)

            val preview = when {
                a.format == "bulletin" -> "Tap to view bulletin"
                !a.bodyMarkdown.isNullOrBlank() -> a.bodyMarkdown!!
                else -> ""
            }
            if (preview.isNotBlank()) Text(preview, maxLines = 3, style = MaterialTheme.typography.bodyMedium)
        }
    }
}
