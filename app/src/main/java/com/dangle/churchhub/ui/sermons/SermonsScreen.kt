package com.dangle.churchhub.ui.sermons

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.dangle.churchhub.core.util.openYouTubeVideo

@Composable
fun SermonsScreen(
    vm: SermonsViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val sermons = vm.sermons.collectAsState().value

    LaunchedEffect(Unit) { vm.refresh() }

    Column(Modifier.fillMaxSize().padding(16.dp)) {
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text("Sermons", style = MaterialTheme.typography.headlineSmall)
            TextButton(onClick = vm::refresh) { Text("Refresh") }
        }

        Spacer(Modifier.height(12.dp))

        LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            items(sermons, key = { it.id }) { s ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { openYouTubeVideo(context, s.youtubeVideoId) }
                ) {
                    Row(Modifier.padding(12.dp), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        AsyncImage(
                            model = s.thumbnailUrl,
                            contentDescription = "YouTube thumbnail",
                            modifier = Modifier.width(140.dp).height(80.dp)
                        )

                        Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                            Text(s.title, style = MaterialTheme.typography.titleMedium)
                            val meta = listOfNotNull(s.speaker, s.series).joinToString(" • ")
                            if (meta.isNotBlank()) Text(meta, style = MaterialTheme.typography.bodySmall)
                            Text("Tap to open YouTube", style = MaterialTheme.typography.labelMedium)
                        }
                    }
                }
            }
        }
    }
}
