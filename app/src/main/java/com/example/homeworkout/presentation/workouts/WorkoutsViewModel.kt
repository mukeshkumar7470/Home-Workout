package com.example.homeworkout.presentation.workouts

import androidx.lifecycle.viewModelScope
import com.example.homeworkout.domain.usecase.GetWorkoutsUseCase
import com.example.homeworkout.presentation.mvi.MviViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.launch

@HiltViewModel
class WorkoutsViewModel @Inject constructor(
    private val getWorkoutsUseCase: GetWorkoutsUseCase,
) : MviViewModel<WorkoutsIntent, WorkoutsState, WorkoutsEffect>(WorkoutsState()) {

    init {
        onIntent(WorkoutsIntent.Load)
    }

    override fun onIntent(intent: WorkoutsIntent) {
        when (intent) {
            WorkoutsIntent.Load -> loadWorkouts()
            is WorkoutsIntent.CategorySelected -> {
                setState { copy(selectedCategory = intent.category) }
                applyFilters()
            }
            is WorkoutsIntent.DifficultySelected -> {
                setState { copy(selectedDifficulty = intent.difficulty) }
                applyFilters()
            }
            is WorkoutsIntent.WorkoutClicked ->
                sendEffect { WorkoutsEffect.NavigateToWorkoutDetail(intent.workoutId) }
        }
    }

    private fun loadWorkouts() {
        viewModelScope.launch {
            setState { copy(isLoading = true, errorMessage = null) }
            runCatching {
                val workouts = getWorkoutsUseCase()
                setState {
                    copy(
                        isLoading = false,
                        workouts = workouts,
                        filteredWorkouts = workouts,
                    )
                }
            }.onFailure { error ->
                setState {
                    copy(
                        isLoading = false,
                        errorMessage = error.message ?: "Failed to load workouts",
                    )
                }
            }
        }
    }

    private fun applyFilters() {
        val current = state.value
        val filtered = current.workouts.filter { workout ->
            (current.selectedCategory == null || workout.category == current.selectedCategory) &&
                (current.selectedDifficulty == null || workout.difficulty == current.selectedDifficulty)
        }
        setState { copy(filteredWorkouts = filtered) }
    }
}
