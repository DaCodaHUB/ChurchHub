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
    val items = vm.announcements.collectAsState().value

    LaunchedEffect(Unit) { vm.refresh() }

    Column(Modifier.fillMaxSize().padding(16.dp)) {
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text("Announcements", style = MaterialTheme.typography.headlineSmall)
            TextButton(onClick = vm::refresh) { Text("Refresh") }
        }

        Spacer(Modifier.height(12.dp))

        LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            items(items, key = { it.id }) { a ->
                Card(Modifier.fillMaxWidth()) {
                    Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text(a.title, style = MaterialTheme.typography.titleMedium)
                            if (a.pinned) {
                                Text("PINNED", style = MaterialTheme.typography.labelSmall)
                            }
                        }
                        Text(a.category, style = MaterialTheme.typography.labelMedium)
                        Text(a.bodyMarkdown, maxLines = 4, style = MaterialTheme.typography.bodyMedium)
                    }
                }
            }
        }
    }
}
