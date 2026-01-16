package com.dangle.churchhub.ui.announcements.detail

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.dangle.churchhub.ui.settings.AppLanguage
import com.dangle.churchhub.ui.settings.SettingsViewModel
import kotlinx.serialization.json.Json

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AnnouncementDetailScreen(
    announcementId: String,
    onBack: () -> Unit,
    vm: AnnouncementDetailViewModel = hiltViewModel(),
    settingsVm: SettingsViewModel = hiltViewModel()
) {
    val a by vm.announcement.collectAsState()
    val settings by settingsVm.settings.collectAsState()
    val lang = settings.language

    LaunchedEffect(announcementId) { vm.load(announcementId) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Announcement") },
                navigationIcon = { TextButton(onClick = onBack) { Text("Back") } }
            )
        }
    ) { padding ->
        if (a == null) {
            Box(Modifier.padding(padding).fillMaxSize()) {
                Text("Loading…", modifier = Modifier.padding(16.dp))
            }
            return@Scaffold
        }

        val announcement = a!!

        val displayTitle =
            if (lang == AppLanguage.EN && !announcement.titleEn.isNullOrBlank()) announcement.titleEn!!
            else announcement.title

        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(displayTitle, style = MaterialTheme.typography.headlineSmall)
            Text(announcement.category, style = MaterialTheme.typography.labelLarge)

            if (announcement.format == "bulletin" && !announcement.bulletinJson.isNullOrBlank()) {
                BulletinRendererHideZoomDetails(
                    bulletinJson = announcement.bulletinJson!!,
                    language = lang
                )
            } else {
                Text(announcement.bodyMarkdown ?: "No details.", style = MaterialTheme.typography.bodyMedium)
            }
        }
    }
}

@Composable
private fun BulletinRendererHideZoomDetails(
    bulletinJson: String,
    language: AppLanguage
) {
    val json = remember {
        Json {
            ignoreUnknownKeys = true
            isLenient = true
        }
    }

    val bulletin = remember(bulletinJson) {
        runCatching { json.decodeFromString(BulletinDto.serializer(), bulletinJson) }.getOrNull()
    }

    if (bulletin == null) {
        Text("Unable to parse bulletin.", color = MaterialTheme.colorScheme.error)
        return
    }

    bulletin.sections.forEach { section ->
        val heading =
            if (language == AppLanguage.EN && !section.headingEn.isNullOrBlank()) section.headingEn!!
            else section.heading

        Text(heading, style = MaterialTheme.typography.titleLarge)

        section.items.forEach { item ->
            val zoomSensitive = isZoomSensitive(item.text) || isZoomSensitive(item.details)

            val safeText = if (zoomSensitive) sanitizeZoomText(item.text) else item.text
            val safeDetails = item.details?.let { d ->
                if (zoomSensitive || isZoomSensitive(d)) sanitizeZoomDetails(d) else d
            }

            Text("• $safeText", style = MaterialTheme.typography.bodyLarge)
            if (!safeDetails.isNullOrBlank()) Text(safeDetails, style = MaterialTheme.typography.bodyMedium)

            item.meta?.forEach { meta ->
                val metaLine = "${meta.label}: ${meta.value}"
                val safeMeta = if (zoomSensitive || isZoomSensitive(metaLine)) sanitizeZoomDetails(metaLine) else metaLine
                if (safeMeta.isNotBlank()) Text(safeMeta, style = MaterialTheme.typography.bodySmall)
            }

            item.subitems?.forEach { sub ->
                val subLine = "${sub.label}: ${sub.text}"
                val safeSub = if (zoomSensitive || isZoomSensitive(subLine)) sanitizeZoomText(subLine) else subLine
                Text("  – $safeSub", style = MaterialTheme.typography.bodyMedium)

                sub.meta?.forEach { meta ->
                    val subMetaLine = "${meta.label}: ${meta.value}"
                    val safeSubMeta =
                        if (zoomSensitive || isZoomSensitive(subMetaLine)) sanitizeZoomDetails(subMetaLine) else subMetaLine
                    if (safeSubMeta.isNotBlank()) Text("     $safeSubMeta", style = MaterialTheme.typography.bodySmall)
                }
            }

            Spacer(Modifier.height(10.dp))
        }

        Divider()
        Spacer(Modifier.height(12.dp))
    }
}

/** Detect Zoom-sensitive content. */
private fun isZoomSensitive(text: String?): Boolean {
    if (text.isNullOrBlank()) return false
    val t = text.lowercase()
    return t.contains("zoom") ||
            t.contains("meeting id") ||
            t.contains("passcode") ||
            t.contains("password") ||
            t.contains("pwd=") ||
            t.contains("zoom.us")
}

private fun sanitizeZoomText(text: String): String {
    return text
        .replace(Regex("\\bon\\s+zoom\\b", RegexOption.IGNORE_CASE), "")
        .replace(Regex("\\btrên\\s+zoom\\b", RegexOption.IGNORE_CASE), "")
        .replace(Regex("\\bzoom\\b", RegexOption.IGNORE_CASE), "")
        .replace(Regex("\\s{2,}"), " ")
        .replace(Regex("\\s+,\\s+"), ", ")
        .replace(Regex("\\s+•\\s+"), " • ")
        .trim()
        .trimEnd(',', '-', '•')
        .trim()
}

private fun sanitizeZoomDetails(details: String): String {
    return details
        .replace(Regex("https?://\\S+"), "")
        .replace(Regex("\\b(Meeting\\s*ID|ID)\\b\\s*[:#]?\\s*\\S+", RegexOption.IGNORE_CASE), "")
        .replace(Regex("\\b(Passcode|Password|Mật\\s*khẩu)\\b\\s*[:#]?\\s*\\S+", RegexOption.IGNORE_CASE), "")
        .replace(Regex("\\b\\S*zoom\\.us\\S*\\b", RegexOption.IGNORE_CASE), "")
        .replace(Regex("\\s{2,}"), " ")
        .replace(Regex("\\s+,\\s+"), ", ")
        .trim()
}
