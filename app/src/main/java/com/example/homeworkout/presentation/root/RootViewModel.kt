package com.example.homeworkout.presentation.root

import androidx.lifecycle.viewModelScope
import com.example.homeworkout.domain.usecase.IsOnboardingCompletedUseCase
import com.example.homeworkout.presentation.mvi.MviViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.launch

@HiltViewModel
class RootViewModel @Inject constructor(
    private val isOnboardingCompletedUseCase: IsOnboardingCompletedUseCase,
) : MviViewModel<RootIntent, RootState, RootEffect>(RootState()) {

    override fun onIntent(intent: RootIntent) {
        when (intent) {
            RootIntent.CheckStartup -> checkStartup()
        }
    }

    fun onOnboardingFinished() {
        setState { copy(showOnboarding = false, isLoading = false) }
    }

    private fun checkStartup() {
        viewModelScope.launch {
            setState { copy(isLoading = true) }
            runCatching {
                val completed = isOnboardingCompletedUseCase()
                setState { copy(isLoading = false, showOnboarding = !completed) }
            }.onFailure {
                setState { copy(isLoading = false, showOnboarding = true) }
            }
        }
    }
}
