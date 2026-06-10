package com.example.homeworkout

import android.app.Application
import com.example.homeworkout.domain.repository.SettingsRepository
import com.example.homeworkout.util.LocaleHelper
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

@HiltAndroidApp
class HomeWorkoutApplication : Application() {

    @Inject lateinit var settingsRepository: SettingsRepository

    override fun onCreate() {
        super.onCreate()
        runBlocking {
            val language = settingsRepository.observeSettings().first().language
            LocaleHelper.syncSystemLocale(language)
        }
    }
}
