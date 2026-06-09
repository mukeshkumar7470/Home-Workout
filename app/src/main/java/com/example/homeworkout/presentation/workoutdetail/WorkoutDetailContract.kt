package com.example.homeworkout.presentation.workoutdetail

import com.example.homeworkout.domain.model.Workout
import com.example.homeworkout.presentation.mvi.MviEffect
import com.example.homeworkout.presentation.mvi.MviIntent
import com.example.homeworkout.presentation.mvi.MviState

sealed interface WorkoutDetailIntent : MviIntent {
    data class Load(val workoutId: String) : WorkoutDetailIntent
    data object StartWorkout : WorkoutDetailIntent
}

data class WorkoutDetailState(
    val isLoading: Boolean = true,
    val workout: Workout? = null,
    val errorMessage: String? = null,
) : MviState

sealed interface WorkoutDetailEffect : MviEffect {
    data class NavigateToActiveWorkout(val workoutId: String) : WorkoutDetailEffect
}
