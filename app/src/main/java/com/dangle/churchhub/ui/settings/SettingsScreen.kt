package com.dangle.churchhub.ui.settings

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun SettingsScreen(
    vm: SettingsViewModel = hiltViewModel()
) {
    val settings by vm.settings.collectAsState()

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text("Settings", style = MaterialTheme.typography.headlineSmall)

        Card {
            Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text("Theme", style = MaterialTheme.typography.titleMedium)

                ThemeRow("System", settings.themeMode == AppThemeMode.SYSTEM) { vm.setTheme(AppThemeMode.SYSTEM) }
                ThemeRow("Light", settings.themeMode == AppThemeMode.LIGHT) { vm.setTheme(AppThemeMode.LIGHT) }
                ThemeRow("Dark", settings.themeMode == AppThemeMode.DARK) { vm.setTheme(AppThemeMode.DARK) }
            }
        }

        Card {
            Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text("Language", style = MaterialTheme.typography.titleMedium)

                ThemeRow("English", settings.language == AppLanguage.EN) { vm.setLanguage(AppLanguage.EN) }
                ThemeRow("Tiếng Việt", settings.language == AppLanguage.VI) { vm.setLanguage(AppLanguage.VI) }
            }
        }
    }
}

@Composable
private fun ThemeRow(label: String, selected: Boolean, onClick: () -> Unit) {
    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
        Text(label)
        RadioButton(selected = selected, onClick = onClick)
    }
}
