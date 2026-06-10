package com.example.homeworkout.presentation.root

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.homeworkout.domain.model.AppLanguage
import com.example.homeworkout.ui.theme.HomeWorkoutTheme
import com.example.homeworkout.util.ProvideAppLocale

@Composable
fun AppRoot(
    modifier: Modifier = Modifier,
    appSettingsViewModel: AppSettingsViewModel = hiltViewModel(),
) {
    val uiState by appSettingsViewModel.uiState.collectAsStateWithLifecycle()

    val settings = when (val s = uiState) {
        is AppSettingsState.Loading -> null
        is AppSettingsState.Success -> s.settings
    }

    ProvideAppLocale(language = settings?.language ?: AppLanguage.ENGLISH) {
        HomeWorkoutTheme(darkTheme = settings?.theme?.isDark ?: false) {
            AppContent(modifier = modifier)
        }
    }
}
