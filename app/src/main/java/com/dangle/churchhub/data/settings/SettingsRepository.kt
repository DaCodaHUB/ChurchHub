package com.dangle.churchhub.data.settings

import com.dangle.churchhub.ui.settings.AppLanguage
import com.dangle.churchhub.ui.settings.AppSettings
import com.dangle.churchhub.ui.settings.AppThemeMode
import kotlinx.coroutines.flow.Flow

interface SettingsRepository {
    val settings: Flow<AppSettings>
    suspend fun setThemeMode(mode: AppThemeMode)
    suspend fun setLanguage(lang: AppLanguage)
}
