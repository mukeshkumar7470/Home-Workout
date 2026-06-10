package com.example.homeworkout.presentation.settings

import androidx.lifecycle.viewModelScope
import com.example.homeworkout.domain.model.AppLanguage
import com.example.homeworkout.domain.model.AppTheme
import com.example.homeworkout.domain.usecase.ObserveAppSettingsUseCase
import com.example.homeworkout.domain.usecase.UpdateAppLanguageUseCase
import com.example.homeworkout.domain.usecase.UpdateAppThemeUseCase
import com.example.homeworkout.presentation.mvi.MviViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

@HiltViewModel
class SettingsViewModel @Inject constructor(
    observeAppSettingsUseCase: ObserveAppSettingsUseCase,
    private val updateAppLanguageUseCase: UpdateAppLanguageUseCase,
    private val updateAppThemeUseCase: UpdateAppThemeUseCase,
) : MviViewModel<SettingsIntent, SettingsState, SettingsEffect>(SettingsState()) {

    init {
        observeAppSettingsUseCase()
            .onEach { settings ->
                setState {
                    copy(
                        isLoading = false,
                        language = settings.language,
                        theme = settings.theme,
                    )
                }
            }
            .launchIn(viewModelScope)
    }

    override fun onIntent(intent: SettingsIntent) {
        when (intent) {
            SettingsIntent.Load -> Unit
            is SettingsIntent.LanguageSelected -> updateLanguage(intent.language)
            is SettingsIntent.ThemeSelected -> updateTheme(intent.theme)
        }
    }

    private fun updateLanguage(language: AppLanguage) {
        if (language == state.value.language) return
        viewModelScope.launch {
            updateAppLanguageUseCase(language)
        }
    }

    private fun updateTheme(theme: AppTheme) {
        if (theme == state.value.theme) return
        viewModelScope.launch { updateAppThemeUseCase(theme) }
    }
}
