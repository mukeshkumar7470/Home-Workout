package com.example.homeworkout.domain.usecase

import com.example.homeworkout.domain.model.AppLanguage
import com.example.homeworkout.domain.model.AppSettings
import com.example.homeworkout.domain.model.AppTheme
import com.example.homeworkout.domain.repository.SettingsRepository
import kotlinx.coroutines.flow.Flow

class ObserveAppSettingsUseCase(
    private val repository: SettingsRepository,
) {
    operator fun invoke(): Flow<AppSettings> = repository.observeSettings()
}

class UpdateAppLanguageUseCase(
    private val repository: SettingsRepository,
) {
    suspend operator fun invoke(language: AppLanguage) = repository.setLanguage(language)
}

class UpdateAppThemeUseCase(
    private val repository: SettingsRepository,
) {
    suspend operator fun invoke(theme: AppTheme) = repository.setTheme(theme)
}
