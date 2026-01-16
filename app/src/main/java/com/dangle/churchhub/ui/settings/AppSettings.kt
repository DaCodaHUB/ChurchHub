package com.dangle.churchhub.ui.settings

enum class AppThemeMode { SYSTEM, LIGHT, DARK }
enum class AppLanguage { EN, VI }

data class AppSettings(
    val themeMode: AppThemeMode = AppThemeMode.SYSTEM,
    val language: AppLanguage = AppLanguage.EN
)