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
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.Checkbox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel

enum class Language { EN, VI }
@Composable
fun ReadingPlanScreen(
    vm: ReadingPlanViewModel = hiltViewModel()
) {
    val state by vm.uiState.collectAsState()

    // Load on first enter
    LaunchedEffect(Unit) { vm.refresh() }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Bible Reading Plan",
                style = MaterialTheme.typography.headlineSmall
            )
            TextButton(onClick = vm::refresh, enabled = !state.isRefreshing) {
                Text(if (state.isRefreshing) "Refreshing…" else "Refresh")
            }
        }

        // Error banner (if any)
        state.error?.let { msg ->
            Text(
                text = "Error: $msg",
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodyMedium
            )
        }

        // Language toggle
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            FilterChip(
                selected = state.language == Language.EN,
                onClick = { vm.setLanguage(Language.EN) },
                label = { Text("English") }
            )
            FilterChip(
                selected = state.language == Language.VI,
                onClick = { vm.setLanguage(Language.VI) },
                label = { Text("Tiếng Việt") }
            )
        }

        // Plan title (show primary in selected language; optional secondary under it)
        val titlePrimary =
            if (state.language == Language.EN) state.planTitle else state.planTitleVi
        val titleSecondary =
            if (state.language == Language.EN) state.planTitleVi else state.planTitle

        if (titlePrimary.isNotBlank()) {
            Text(titlePrimary, style = MaterialTheme.typography.titleLarge)
            if (titleSecondary.isNotBlank() && titleSecondary != titlePrimary) {
                Text(titleSecondary, style = MaterialTheme.typography.bodyMedium)
            }
        }

        Spacer(Modifier.height(4.dp))

        // List
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(state.rows, key = { it.item.id }) { row ->
                val item = row.item

                val displayDate =
                    if (state.language == Language.EN) item.displayDate else item.displayDateVi
                val reading =
                    if (state.language == Language.EN) item.reading else item.readingVi
                val psalm =
                    if (state.language == Language.EN) item.psalm else item.psalmVi

                Card(modifier = Modifier.fillMaxWidth()) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column(
                            modifier = Modifier.weight(1f),
                            verticalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Text(
                                text = "Day ${item.dayNumber} • $displayDate",
                                style = MaterialTheme.typography.labelLarge
                            )
                            Text(
                                text = reading,
                                style = MaterialTheme.typography.titleMedium
                            )
                            Text(
                                text = psalm,
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }

                        Checkbox(
                            checked = row.isCompleted,
                            onCheckedChange = { checked ->
                                vm.setCompleted(item.id, checked)
                            }
                        )
                    }
                }
            }
        }
    }
}
