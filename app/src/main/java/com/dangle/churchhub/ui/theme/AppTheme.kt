package com.dangle.churchhub.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import com.dangle.churchhub.ui.settings.AppThemeMode

@Composable
fun ChurchHubTheme(
    themeMode: AppThemeMode,
    content: @Composable () -> Unit
) {
    val dark = when (themeMode) {
        AppThemeMode.SYSTEM -> isSystemInDarkTheme()
        AppThemeMode.DARK -> true
        AppThemeMode.LIGHT -> false
    }

    // If you already have ColorScheme setup, plug it in here.
    // For now, keep default MaterialTheme (or your generated theme).
    MaterialTheme(
        content = content
    )
}
