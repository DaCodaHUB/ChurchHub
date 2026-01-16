package com.dangle.churchhub.data.settings

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.dangle.churchhub.ui.settings.AppLanguage
import com.dangle.churchhub.ui.settings.AppSettings
import com.dangle.churchhub.ui.settings.AppThemeMode
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

private val Context.dataStore by preferencesDataStore(name = "app_settings")

@Singleton
class SettingsRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context
) : SettingsRepository {

    private object Keys {
        val THEME = stringPreferencesKey("theme_mode")
        val LANG = stringPreferencesKey("app_language")
    }

    override val settings: Flow<AppSettings> =
        context.dataStore.data.map { prefs ->
            val theme = prefs[Keys.THEME]?.let { runCatching { AppThemeMode.valueOf(it) }.getOrNull() }
                ?: AppThemeMode.SYSTEM
            val lang = prefs[Keys.LANG]?.let { runCatching { AppLanguage.valueOf(it) }.getOrNull() }
                ?: AppLanguage.EN
            AppSettings(themeMode = theme, language = lang)
        }

    override suspend fun setThemeMode(mode: AppThemeMode) {
        context.dataStore.edit { it[Keys.THEME] = mode.name }
    }

    override suspend fun setLanguage(lang: AppLanguage) {
        context.dataStore.edit { it[Keys.LANG] = lang.name }
    }
}
