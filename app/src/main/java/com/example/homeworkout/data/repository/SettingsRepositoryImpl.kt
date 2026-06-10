package com.example.homeworkout.data.repository

import com.example.homeworkout.data.local.SettingsDataStore
import com.example.homeworkout.domain.model.AppLanguage
import com.example.homeworkout.domain.model.AppSettings
import com.example.homeworkout.domain.model.AppTheme
import com.example.homeworkout.domain.repository.SettingsRepository
import kotlinx.coroutines.flow.Flow

class SettingsRepositoryImpl(
    private val settingsDataStore: SettingsDataStore,
) : SettingsRepository {
    override fun observeSettings(): Flow<AppSettings> = settingsDataStore.observeSettings()
    override suspend fun setLanguage(language: AppLanguage) = settingsDataStore.setLanguage(language)
    override suspend fun setTheme(theme: AppTheme) = settingsDataStore.setTheme(theme)
}
