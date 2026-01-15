package com.dangle.churchhub.ui.announcements.detail

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.Divider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import kotlinx.serialization.json.Json

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AnnouncementDetailScreen(
    announcementId: String,
    onBack: () -> Unit,
    vm: AnnouncementDetailViewModel = hiltViewModel()
) {
    val a by vm.announcement.collectAsState()

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

        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(announcement.title, style = MaterialTheme.typography.headlineSmall)
            Text(announcement.category, style = MaterialTheme.typography.labelLarge)

            // Bulletin format
            if (announcement.format == "bulletin" && !announcement.bulletinJson.isNullOrBlank()) {
                BulletinRendererHideZoomDetails(bulletinJson = announcement.bulletinJson!!)
            } else {
                // Simple announcement format
                Text(announcement.bodyMarkdown ?: "No details.", style = MaterialTheme.typography.bodyMedium)
            }
        }
    }
}

/**
 * Option A: keep the item + time/date, but remove Zoom references and any access details.
 */
@Composable
private fun BulletinRendererHideZoomDetails(bulletinJson: String) {
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
        Text(section.heading, style = MaterialTheme.typography.titleLarge)

        section.items.forEach { item ->
            val zoomSensitive = isZoomSensitive(item.text) || isZoomSensitive(item.details)

            val safeText = if (zoomSensitive) sanitizeZoomText(item.text) else item.text
            val safeDetails = item.details?.let { d ->
                if (zoomSensitive || isZoomSensitive(d)) sanitizeZoomDetails(d) else d
            }

            // Main bullet item
            Text("• $safeText", style = MaterialTheme.typography.bodyLarge)
            if (!safeDetails.isNullOrBlank()) {
                Text(safeDetails, style = MaterialTheme.typography.bodyMedium)
            }

            // Meta
            item.meta?.forEach { meta ->
                val metaLine = "${meta.label}: ${meta.value}"
                val safeMeta = if (zoomSensitive || isZoomSensitive(metaLine)) sanitizeZoomDetails(metaLine) else metaLine
                if (safeMeta.isNotBlank()) {
                    Text(safeMeta, style = MaterialTheme.typography.bodySmall)
                }
            }

            // Subitems
            item.subitems?.forEach { sub ->
                val subLine = "${sub.label}: ${sub.text}"
                val safeSub = if (zoomSensitive || isZoomSensitive(subLine)) sanitizeZoomText(subLine) else subLine
                Text("  – $safeSub", style = MaterialTheme.typography.bodyMedium)

                sub.meta?.forEach { meta ->
                    val subMetaLine = "${meta.label}: ${meta.value}"
                    val safeSubMeta =
                        if (zoomSensitive || isZoomSensitive(subMetaLine)) sanitizeZoomDetails(subMetaLine) else subMetaLine
                    if (safeSubMeta.isNotBlank()) {
                        Text("     $safeSubMeta", style = MaterialTheme.typography.bodySmall)
                    }
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
    // Add more keywords here if you ever include access info.
    return t.contains("zoom") ||
            t.contains("meeting id") ||
            t.contains("passcode") ||
            t.contains("password") ||
            t.contains("pwd=") ||
            t.contains("zoom.us")
}

/**
 * Removes the word Zoom and common phrases like "on Zoom" / "trên Zoom",
 * while keeping the rest of the sentence intact.
 */
private fun sanitizeZoomText(text: String): String {
    return text
        // remove "on Zoom" or "trên Zoom" phrases
        .replace(Regex("\\bon\\s+zoom\\b", RegexOption.IGNORE_CASE), "")
        .replace(Regex("\\btrên\\s+zoom\\b", RegexOption.IGNORE_CASE), "")
        // remove standalone "Zoom"
        .replace(Regex("\\bzoom\\b", RegexOption.IGNORE_CASE), "")
        // clean punctuation leftovers
        .replace(Regex("\\s{2,}"), " ")
        .replace(Regex("\\s+,\\s+"), ", ")
        .replace(Regex("\\s+•\\s+"), " • ")
        .trim()
        .trimEnd(',', '-', '•')
        .trim()
}

/**
 * Removes URLs and any obvious access info (Meeting ID / passcode),
 * but leaves time/date details intact.
 */
private fun sanitizeZoomDetails(details: String): String {
    return details
        // remove URLs (Zoom links or any links)
        .replace(Regex("https?://\\S+"), "")
        // remove "Meeting ID: ..." / "Passcode: ..." patterns if ever present
        .replace(Regex("\\b(Meeting\\s*ID|ID)\\b\\s*[:#]?\\s*\\S+", RegexOption.IGNORE_CASE), "")
        .replace(Regex("\\b(Passcode|Password|Mật\\s*khẩu)\\b\\s*[:#]?\\s*\\S+", RegexOption.IGNORE_CASE), "")
        // remove zoom.us fragments
        .replace(Regex("\\b\\S*zoom\\.us\\S*\\b", RegexOption.IGNORE_CASE), "")
        // cleanup whitespace
        .replace(Regex("\\s{2,}"), " ")
        .replace(Regex("\\s+,\\s+"), ", ")
        .trim()
}
