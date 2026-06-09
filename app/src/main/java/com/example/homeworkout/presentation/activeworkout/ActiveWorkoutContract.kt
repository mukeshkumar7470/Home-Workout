package com.example.homeworkout.presentation.activeworkout

import com.example.homeworkout.domain.model.ActiveWorkoutState
import com.example.homeworkout.domain.model.Workout
import com.example.homeworkout.presentation.mvi.MviEffect
import com.example.homeworkout.presentation.mvi.MviIntent
import com.example.homeworkout.presentation.mvi.MviState

sealed interface ActiveWorkoutIntent : MviIntent {
    data class Load(val workoutId: String) : ActiveWorkoutIntent
    data object Tick : ActiveWorkoutIntent
    data object TogglePause : ActiveWorkoutIntent
    data object SkipPhase : ActiveWorkoutIntent
    data object FinishWorkout : ActiveWorkoutIntent
}

data class ActiveWorkoutUiState(
    val isLoading: Boolean = true,
    val activeWorkout: ActiveWorkoutState? = null,
    val isCompleted: Boolean = false,
    val errorMessage: String? = null,
) : MviState {
    val workout: Workout? get() = activeWorkout?.workout
}

sealed interface ActiveWorkoutEffect : MviEffect {
    data object NavigateBack : ActiveWorkoutEffect
    data class ShowCompletion(val workoutName: String, val calories: Int) : ActiveWorkoutEffect
}
