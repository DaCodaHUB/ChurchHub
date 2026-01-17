package com.dangle.churchhub.ui.home

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun HomeScreen(
    contentPadding: PaddingValues,
    vm: HomeViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val church = vm.churchInfo.collectAsState().value

    LaunchedEffect(Unit) { vm.refresh() }

    Column(
        modifier = Modifier.fillMaxSize().padding(contentPadding).padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text("Home", style = MaterialTheme.typography.headlineSmall)
            TextButton(onClick = vm::refresh) { Text("Refresh") }
        }

        if (church == null) {
            Text("Loading church info…", style = MaterialTheme.typography.bodyMedium)
            return@Column
        }

        Text(church.name, style = MaterialTheme.typography.headlineSmall)
        church.tagline?.let { Text(it, style = MaterialTheme.typography.titleSmall) }

        Card {
            Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text("Location", style = MaterialTheme.typography.titleMedium)
                val addr = "${church.addressLine1}, ${church.city}, ${church.state} ${church.zip}"
                Text(addr)

                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    OutlinedButton(onClick = {
                        val uri = Uri.parse("geo:0,0?q=${Uri.encode(addr)}")
                        context.startActivity(Intent(Intent.ACTION_VIEW, uri))
                    }) { Text("Open Maps") }

                    church.website?.let { url ->
                        OutlinedButton(onClick = {
                            context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))
                        }) { Text("Website") }
                    }
                }
            }
        }

        Card {
            Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text("Contact", style = MaterialTheme.typography.titleMedium)

                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    church.phone?.let { phone ->
                        OutlinedButton(onClick = {
                            context.startActivity(Intent(Intent.ACTION_DIAL, Uri.parse("tel:$phone")))
                        }) { Text("Call") }
                    }
                    church.email?.let { email ->
                        OutlinedButton(onClick = {
                            context.startActivity(Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:$email")))
                        }) { Text("Email") }
                    }
                }
            }
        }

        Card {
            Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text("Links", style = MaterialTheme.typography.titleMedium)

                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    church.giving?.let { url ->
                        OutlinedButton(onClick = {
                            context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))
                        }) { Text("Give") }
                    }
                    church.youtubeChannel?.let { url ->
                        OutlinedButton(onClick = {
                            context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))
                        }) { Text("YouTube") }
                    }
                }
            }
        }
    }
}
