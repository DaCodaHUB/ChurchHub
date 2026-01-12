package com.dangle.churchhub.ui.announcements

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun AnnouncementsScreen(
    vm: AnnouncementsViewModel = hiltViewModel()
) {
    val state by vm.uiState.collectAsState()

    Column(Modifier.fillMaxSize()) {

        if (state.error != null) {
            Text(
                text = "Sync failed: ${state.error}",
                modifier = Modifier.padding(16.dp),
                color = MaterialTheme.colorScheme.error
            )
        }

        Row(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text("Announcements", style = MaterialTheme.typography.titleLarge)

            TextButton(onClick = { vm.refresh() }, enabled = !state.isRefreshing) {
                Text(if (state.isRefreshing) "Refreshing…" else "Refresh")
            }
        }

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(state.items, key = { it.id }) { a ->
                Card {
                    Column(Modifier.padding(16.dp)) {
                        Text(a.title, style = MaterialTheme.typography.titleMedium)
                        Spacer(Modifier.height(4.dp))
                        Text(a.category, style = MaterialTheme.typography.labelMedium)
                        Spacer(Modifier.height(8.dp))
                        Text(a.bodyMarkdown, maxLines = 3)
                    }
                }
            }
        }
    }
}
