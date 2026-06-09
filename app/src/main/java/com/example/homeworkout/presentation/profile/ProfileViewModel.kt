package com.example.homeworkout.presentation.profile

import androidx.lifecycle.viewModelScope
import com.example.homeworkout.domain.usecase.GetUserProgressUseCase
import com.example.homeworkout.presentation.mvi.MviViewModel
import kotlinx.coroutines.launch

class ProfileViewModel(
    private val getUserProgressUseCase: GetUserProgressUseCase,
) : MviViewModel<ProfileIntent, ProfileState, ProfileEffect>(ProfileState()) {

    init {
        onIntent(ProfileIntent.Load)
    }

    override fun onIntent(intent: ProfileIntent) {
        when (intent) {
            ProfileIntent.Load -> loadProfile()
        }
    }

    private fun loadProfile() {
        viewModelScope.launch {
            setState { copy(isLoading = true, errorMessage = null) }
            runCatching {
                val progress = getUserProgressUseCase()
                setState { copy(isLoading = false, progress = progress) }
            }.onFailure { error ->
                setState {
                    copy(
                        isLoading = false,
                        errorMessage = error.message ?: "Failed to load profile",
                    )
                }
            }
        }
    }
}
