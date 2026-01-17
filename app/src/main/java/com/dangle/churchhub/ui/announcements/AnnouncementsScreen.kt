package com.dangle.churchhub.ui.announcements

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.dangle.churchhub.data.remote.dto.AnnouncementsDto
import com.dangle.churchhub.ui.settings.AppLanguage
import com.dangle.churchhub.ui.settings.SettingsViewModel
import kotlinx.serialization.json.Json

@Composable
fun AnnouncementsScreen(
    contentPadding: PaddingValues,
    vm: AnnouncementsViewModel = hiltViewModel(),
    settingsVm: SettingsViewModel = hiltViewModel()
) {
    val state by vm.uiState.collectAsState()
    val settings by settingsVm.settings.collectAsState()
    val lang = settings.language

    val json = remember { Json { ignoreUnknownKeys = true; isLenient = true } }

    val bulletin = remember(state.bulletinJson) {
        state.bulletinJson?.let {
            runCatching { json.decodeFromString(AnnouncementsDto.serializer(), it) }.getOrNull()
        }
    }

    Column(Modifier.fillMaxSize().padding(contentPadding).padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text("Announcements", style = MaterialTheme.typography.headlineSmall)
            TextButton(onClick = vm::refresh, enabled = !state.isRefreshing) {
                Text(if (state.isRefreshing) "Refreshing…" else "Refresh")
            }
        }

        state.error?.let { msg ->
            Text("Error: $msg", color = MaterialTheme.colorScheme.error)
        }

        if (bulletin == null) {
            Text("Loading…")
            return@Column
        }

        Column(
            modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            bulletin.announcements.forEach { section ->
                val heading = if (lang == AppLanguage.EN) section.headingEn else section.headingVi
                Text(heading, style = MaterialTheme.typography.titleLarge)

                section.items.forEach { item ->
                    val text = if (lang == AppLanguage.EN) item.textEn else item.textVi
                    val details = if (lang == AppLanguage.EN) item.detailsEn else item.detailsVi

                    Text("• $text", style = MaterialTheme.typography.bodyLarge)
                    details?.takeIf { it.isNotBlank() }?.let {
                        Text(it, style = MaterialTheme.typography.bodyMedium)
                    }

                    item.meta?.forEach { meta ->
                        val label = if (lang == AppLanguage.EN) meta.labelEn else meta.labelVi
                        Text("$label: ${meta.value}", style = MaterialTheme.typography.bodySmall)
                    }

                    item.subitems?.forEach { sub ->
                        val label = if (lang == AppLanguage.EN) sub.labelEn else sub.labelVi
                        val subText = if (lang == AppLanguage.EN) sub.textEn else sub.textVi
                        Text("  – $label: $subText", style = MaterialTheme.typography.bodyMedium)
                    }

                    Spacer(Modifier.height(10.dp))
                }

                Divider()
            }
        }
    }
}
