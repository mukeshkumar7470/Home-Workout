package com.example.homeworkout.data.local

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.homeworkout.domain.model.AppLanguage
import com.example.homeworkout.domain.model.AppSettings
import com.example.homeworkout.domain.model.AppTheme
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.settingsDataStore: DataStore<Preferences> by preferencesDataStore(
    name = "app_settings",
)

class SettingsDataStore(context: Context) {
    private val dataStore = context.applicationContext.settingsDataStore

    fun observeSettings(): Flow<AppSettings> = dataStore.data.map { prefs ->
        AppSettings(
            language = AppLanguage.fromCode(prefs[LANGUAGE_KEY] ?: AppLanguage.ENGLISH.code),
            theme = AppTheme.fromRaw(prefs[THEME_KEY] ?: AppTheme.LIGHT.name),
        )
    }

    suspend fun setLanguage(language: AppLanguage) {
        dataStore.edit { it[LANGUAGE_KEY] = language.code }
    }

    suspend fun setTheme(theme: AppTheme) {
        dataStore.edit { it[THEME_KEY] = theme.name }
    }

    companion object {
        private val LANGUAGE_KEY = stringPreferencesKey("language")
        private val THEME_KEY = stringPreferencesKey("theme")
    }
}
