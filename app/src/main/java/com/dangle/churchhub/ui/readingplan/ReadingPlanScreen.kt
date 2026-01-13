package com.dangle.churchhub.ui.readingplan

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun ReadingPlanScreen(
    vm: ReadingPlanViewModel = hiltViewModel()
) {
    val rows = vm.rows.collectAsState().value

    LaunchedEffect(Unit) { vm.refresh() }

    Column(Modifier.fillMaxSize().padding(16.dp)) {
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text("Bible Reading Plan", style = MaterialTheme.typography.headlineSmall)
            TextButton(onClick = vm::refresh) { Text("Refresh") }
        }

        Spacer(Modifier.height(12.dp))

        LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            items(rows, key = { it.item.id }) { row ->
                Card {
                    Row(
                        Modifier.fillMaxWidth().padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                            Text("Day ${row.item.dayNumber}", style = MaterialTheme.typography.labelLarge)
                            Text(row.item.reading, style = MaterialTheme.typography.titleMedium)
                            row.item.psalm?.let { Text(it, style = MaterialTheme.typography.bodyMedium) }
                        }
                        Checkbox(
                            checked = row.isCompleted,
                            onCheckedChange = { checked -> vm.setCompleted(row.item.id, checked) }
                        )
                    }
                }
            }
        }
    }
}
