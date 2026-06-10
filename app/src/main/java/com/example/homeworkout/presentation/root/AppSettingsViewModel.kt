package com.example.homeworkout.presentation.root

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.homeworkout.domain.model.AppSettings
import com.example.homeworkout.domain.usecase.ObserveAppSettingsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

sealed interface AppSettingsState {
    data object Loading : AppSettingsState
    data class Success(val settings: AppSettings) : AppSettingsState
}

@HiltViewModel
class AppSettingsViewModel @Inject constructor(
    observeAppSettingsUseCase: ObserveAppSettingsUseCase,
) : ViewModel() {

    val uiState: StateFlow<AppSettingsState> = observeAppSettingsUseCase()
        .map { AppSettingsState.Success(it) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
            initialValue = AppSettingsState.Loading,
        )
}
