package com.example.homeworkout.presentation.onboarding

import androidx.lifecycle.viewModelScope
import com.example.homeworkout.domain.model.UserProfile
import com.example.homeworkout.domain.usecase.SaveUserProfileUseCase
import com.example.homeworkout.presentation.mvi.MviViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.launch

@HiltViewModel
class OnboardingViewModel @Inject constructor(
    private val saveUserProfileUseCase: SaveUserProfileUseCase,
) : MviViewModel<OnboardingIntent, OnboardingState, OnboardingEffect>(OnboardingState()) {

    init {
        onIntent(OnboardingIntent.Load)
    }

    override fun onIntent(intent: OnboardingIntent) {
        when (intent) {
            OnboardingIntent.Load -> setState { copy(isLoading = false) }
            OnboardingIntent.Next -> handleNext()
            OnboardingIntent.Back -> handleBack()
            OnboardingIntent.Skip -> completeOnboarding(skipDefaults = true)
            is OnboardingIntent.AgeChanged -> setState {
                copy(ageInput = intent.age.filter { it.isDigit() }.take(3), errorMessage = null)
            }
            is OnboardingIntent.WeightChanged -> setState {
                copy(
                    weightInput = intent.weight.filter { it.isDigit() || it == '.' }.take(6),
                    errorMessage = null,
                )
            }
            is OnboardingIntent.GoalSelected -> setState {
                copy(selectedGoal = intent.goal, errorMessage = null)
            }
            is OnboardingIntent.LevelSelected -> setState {
                copy(selectedLevel = intent.level, errorMessage = null)
            }
        }
    }

    private fun handleNext() {
        val current = state.value
        when (current.step) {
            OnboardingStep.WELCOME -> setState { copy(step = OnboardingStep.AGE) }
            OnboardingStep.AGE -> {
                val age = current.ageInput.toIntOrNull()
                if (age == null || age !in 13..100) {
                    setState { copy(errorMessage = "Enter a valid age between 13 and 100") }
                    return
                }
                setState { copy(step = OnboardingStep.WEIGHT, errorMessage = null) }
            }
            OnboardingStep.WEIGHT -> {
                val weight = current.weightInput.toFloatOrNull()
                if (weight == null || weight !in 30f..250f) {
                    setState { copy(errorMessage = "Enter a valid weight between 30 and 250 kg") }
                    return
                }
                setState { copy(step = OnboardingStep.GOAL, errorMessage = null) }
            }
            OnboardingStep.GOAL -> {
                if (current.selectedGoal == null) {
                    setState { copy(errorMessage = "Select a fitness goal") }
                    return
                }
                setState { copy(step = OnboardingStep.LEVEL, errorMessage = null) }
            }
            OnboardingStep.LEVEL -> {
                if (current.selectedLevel == null) {
                    setState { copy(errorMessage = "Select your fitness level") }
                    return
                }
                completeOnboarding(skipDefaults = false)
            }
        }
    }

    private fun handleBack() {
        val previous = when (state.value.step) {
            OnboardingStep.WELCOME -> OnboardingStep.WELCOME
            OnboardingStep.AGE -> OnboardingStep.WELCOME
            OnboardingStep.WEIGHT -> OnboardingStep.AGE
            OnboardingStep.GOAL -> OnboardingStep.WEIGHT
            OnboardingStep.LEVEL -> OnboardingStep.GOAL
        }
        setState { copy(step = previous, errorMessage = null) }
    }

    private fun completeOnboarding(skipDefaults: Boolean) {
        viewModelScope.launch {
            val current = state.value
            val profile = UserProfile(
                age = if (skipDefaults) null else current.ageInput.toIntOrNull(),
                weightKg = if (skipDefaults) null else current.weightInput.toFloatOrNull(),
                fitnessGoal = if (skipDefaults) null else current.selectedGoal,
                fitnessLevel = if (skipDefaults) null else current.selectedLevel,
                onboardingCompleted = true,
            )
            saveUserProfileUseCase(profile)
            sendEffect { OnboardingEffect.NavigateToMain }
        }
    }
}
