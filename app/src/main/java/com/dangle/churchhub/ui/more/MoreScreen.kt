package com.dangle.churchhub.ui.more

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun MoreScreen(
    contentPadding: PaddingValues,
    onOpenSettings: () -> Unit,
    onOpenReadingPlan: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize().padding(contentPadding).padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text("More", style = MaterialTheme.typography.headlineSmall)

        Card(
            modifier = Modifier.fillMaxWidth().clickable(onClick = onOpenReadingPlan)
        ) {
            Column(Modifier.padding(16.dp)) {
                Text("Bible Reading Plan", style = MaterialTheme.typography.titleMedium)
                Text("View & mark completed", style = MaterialTheme.typography.bodyMedium)
            }
        }

        Card(
            modifier = Modifier.fillMaxWidth().clickable(onClick = onOpenSettings)
        ) {
            Column(Modifier.padding(16.dp)) {
                Text("Settings", style = MaterialTheme.typography.titleMedium)
                Text("Theme & language", style = MaterialTheme.typography.bodyMedium)
            }
        }

        Card(modifier = Modifier.fillMaxWidth()) {
            Column(Modifier.padding(16.dp)) {
                Text("Prayer Board", style = MaterialTheme.typography.titleMedium)
                Text("Coming soon", style = MaterialTheme.typography.bodyMedium)
            }
        }

        Card(modifier = Modifier.fillMaxWidth()) {
            Column(Modifier.padding(16.dp)) {
                Text("Ministry Calendar", style = MaterialTheme.typography.titleMedium)
                Text("Coming soon", style = MaterialTheme.typography.bodyMedium)
            }
        }
    }
}
