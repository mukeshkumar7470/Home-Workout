package com.example.homeworkout.presentation.profile

import androidx.lifecycle.viewModelScope
import com.example.homeworkout.domain.usecase.GetUserProfileUseCase
import com.example.homeworkout.domain.usecase.GetUserProgressUseCase
import com.example.homeworkout.presentation.mvi.MviViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.launch

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val getUserProgressUseCase: GetUserProgressUseCase,
    private val getUserProfileUseCase: GetUserProfileUseCase,
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
                val profile = getUserProfileUseCase()
                setState { copy(isLoading = false, progress = progress, profile = profile) }
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
