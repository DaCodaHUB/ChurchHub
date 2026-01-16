package com.dangle.churchhub

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.collectAsState
import com.dangle.churchhub.ui.nav.MainScaffold
import com.dangle.churchhub.ui.theme.ChurchHubTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val vm: com.dangle.churchhub.ui.settings.SettingsViewModel = androidx.hilt.navigation.compose.hiltViewModel()
            val settings = vm.settings.collectAsState().value

            ChurchHubTheme(themeMode = settings.themeMode) {
                MainScaffold()
            }
        }
    }
}

