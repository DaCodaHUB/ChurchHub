package com.dangle.churchhub.ui.readingplan

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
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.dangle.churchhub.ui.settings.AppLanguage

@Composable
fun ReadingPlanScreen(
    vm: ReadingPlanViewModel = hiltViewModel()
) {
    val state by vm.uiState.collectAsState()

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text("Bible Reading Plan", style = MaterialTheme.typography.headlineSmall)
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

        val titlePrimary = if (state.language == AppLanguage.EN) state.planTitle else state.planTitleVi
        val titleSecondary = if (state.language == AppLanguage.EN) state.planTitleVi else state.planTitle

        if (titlePrimary.isNotBlank()) {
            Text(titlePrimary, style = MaterialTheme.typography.titleLarge)
            if (titleSecondary.isNotBlank() && titleSecondary != titlePrimary) {
                Text(titleSecondary, style = MaterialTheme.typography.bodyMedium)
            }
        }

        Spacer(Modifier.height(4.dp))

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(state.rows, key = { it.item.id }) { row ->
                val item = row.item

                val displayDate =
                    if (state.language == AppLanguage.EN) item.displayDate else item.displayDateVi
                val reading =
                    if (state.language == AppLanguage.EN) item.reading else item.readingVi
                val psalm =
                    if (state.language == AppLanguage.EN) item.psalm else item.psalmVi

                Card(Modifier.fillMaxWidth()) {
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                            Text("Day ${item.dayNumber} • $displayDate", style = MaterialTheme.typography.labelLarge)
                            Text(reading, style = MaterialTheme.typography.titleMedium)
                            Text(psalm, style = MaterialTheme.typography.bodyMedium)
                        }
                        Checkbox(
                            checked = row.isCompleted,
                            onCheckedChange = { checked -> vm.setCompleted(item.id, checked) }
                        )
                    }
                }
            }
        }
    }
}
