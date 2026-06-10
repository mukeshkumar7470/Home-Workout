package com.example.homeworkout.presentation.onboarding

import com.example.homeworkout.domain.model.FitnessGoal
import com.example.homeworkout.domain.model.FitnessLevel
import com.example.homeworkout.presentation.mvi.MviEffect
import com.example.homeworkout.presentation.mvi.MviIntent
import com.example.homeworkout.presentation.mvi.MviState

enum class OnboardingStep {
    WELCOME,
    AGE,
    WEIGHT,
    GOAL,
    LEVEL,
}

sealed interface OnboardingIntent : MviIntent {
    data object Load : OnboardingIntent
    data object Next : OnboardingIntent
    data object Back : OnboardingIntent
    data object Skip : OnboardingIntent
    data class AgeChanged(val age: String) : OnboardingIntent
    data class WeightChanged(val weight: String) : OnboardingIntent
    data class GoalSelected(val goal: FitnessGoal) : OnboardingIntent
    data class LevelSelected(val level: FitnessLevel) : OnboardingIntent
}

data class OnboardingState(
    val isLoading: Boolean = true,
    val step: OnboardingStep = OnboardingStep.WELCOME,
    val ageInput: String = "",
    val weightInput: String = "",
    val selectedGoal: FitnessGoal? = null,
    val selectedLevel: FitnessLevel? = null,
    val errorMessage: String? = null,
    val canProceed: Boolean = true,
) : MviState

sealed interface OnboardingEffect : MviEffect {
    data object NavigateToMain : OnboardingEffect
}
