package com.example.homeworkout.domain.repository

import com.example.homeworkout.domain.model.AppLanguage
import com.example.homeworkout.domain.model.AppSettings
import com.example.homeworkout.domain.model.AppTheme
import kotlinx.coroutines.flow.Flow

interface SettingsRepository {
    fun observeSettings(): Flow<AppSettings>
    suspend fun setLanguage(language: AppLanguage)
    suspend fun setTheme(theme: AppTheme)
}
