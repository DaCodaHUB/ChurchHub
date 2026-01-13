package com.dangle.churchhub.ui.sermons

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun SermonsScreen(
    onSermonClick: (String) -> Unit,
    vm: SermonsViewModel = hiltViewModel()
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
            Text("Sermons", style = MaterialTheme.typography.titleLarge)
            TextButton(onClick = { vm.refresh() }, enabled = !state.isRefreshing) {
                Text(if (state.isRefreshing) "Refreshing…" else "Refresh")
            }
        }

        OutlinedTextField(
            value = state.query,
            onValueChange = vm::onQueryChange,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            singleLine = true,
            label = { Text("Search") }
        )

        Spacer(Modifier.height(12.dp))

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(state.items, key = { it.id }) { s ->
                Card(onClick = { onSermonClick(s.id) }) {
                    Column(Modifier.padding(16.dp)) {
                        Text(s.title, style = MaterialTheme.typography.titleMedium)
                        Spacer(Modifier.height(4.dp))
                        Text(
                            text = listOfNotNull(s.speaker, s.series).joinToString(" • "),
                            style = MaterialTheme.typography.labelMedium
                        )
                        Spacer(Modifier.height(8.dp))
                        Text(s.audioUrl, maxLines = 1, style = MaterialTheme.typography.bodySmall)
                    }
                }
            }
        }
    }
}
