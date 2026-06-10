package com.example.homeworkout.presentation.settings

import com.example.homeworkout.domain.model.AppLanguage
import com.example.homeworkout.domain.model.AppTheme
import com.example.homeworkout.presentation.mvi.MviEffect
import com.example.homeworkout.presentation.mvi.MviIntent
import com.example.homeworkout.presentation.mvi.MviState

sealed interface SettingsIntent : MviIntent {
    data object Load : SettingsIntent
    data class LanguageSelected(val language: AppLanguage) : SettingsIntent
    data class ThemeSelected(val theme: AppTheme) : SettingsIntent
}

data class SettingsState(
    val isLoading: Boolean = true,
    val language: AppLanguage = AppLanguage.ENGLISH,
    val theme: AppTheme = AppTheme.LIGHT,
) : MviState

sealed interface SettingsEffect : MviEffect
