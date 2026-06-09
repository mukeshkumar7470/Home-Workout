package com.example.homeworkout.presentation.workoutdetail

import androidx.lifecycle.viewModelScope
import com.example.homeworkout.domain.usecase.GetWorkoutByIdUseCase
import com.example.homeworkout.presentation.mvi.MviViewModel
import kotlinx.coroutines.launch

class WorkoutDetailViewModel(
    private val getWorkoutByIdUseCase: GetWorkoutByIdUseCase,
) : MviViewModel<WorkoutDetailIntent, WorkoutDetailState, WorkoutDetailEffect>(WorkoutDetailState()) {

    override fun onIntent(intent: WorkoutDetailIntent) {
        when (intent) {
            is WorkoutDetailIntent.Load -> loadWorkout(intent.workoutId)
            WorkoutDetailIntent.StartWorkout -> {
                val workoutId = state.value.workout?.id ?: return
                sendEffect { WorkoutDetailEffect.NavigateToActiveWorkout(workoutId) }
            }
        }
    }

    private fun loadWorkout(workoutId: String) {
        viewModelScope.launch {
            setState { copy(isLoading = true, errorMessage = null) }
            runCatching {
                val workout = getWorkoutByIdUseCase(workoutId)
                setState {
                    copy(
                        isLoading = false,
                        workout = workout,
                        errorMessage = if (workout == null) "Workout not found" else null,
                    )
                }
            }.onFailure { error ->
                setState {
                    copy(
                        isLoading = false,
                        errorMessage = error.message ?: "Failed to load workout",
                    )
                }
            }
        }
    }
}
