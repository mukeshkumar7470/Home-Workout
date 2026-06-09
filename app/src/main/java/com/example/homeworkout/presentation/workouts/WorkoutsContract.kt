package com.example.homeworkout.presentation.workouts

import com.example.homeworkout.domain.model.Difficulty
import com.example.homeworkout.domain.model.Workout
import com.example.homeworkout.domain.model.WorkoutCategory
import com.example.homeworkout.presentation.mvi.MviEffect
import com.example.homeworkout.presentation.mvi.MviIntent
import com.example.homeworkout.presentation.mvi.MviState

sealed interface WorkoutsIntent : MviIntent {
    data object Load : WorkoutsIntent
    data class CategorySelected(val category: WorkoutCategory?) : WorkoutsIntent
    data class DifficultySelected(val difficulty: Difficulty?) : WorkoutsIntent
    data class WorkoutClicked(val workoutId: String) : WorkoutsIntent
}

data class WorkoutsState(
    val isLoading: Boolean = true,
    val workouts: List<Workout> = emptyList(),
    val filteredWorkouts: List<Workout> = emptyList(),
    val selectedCategory: WorkoutCategory? = null,
    val selectedDifficulty: Difficulty? = null,
    val errorMessage: String? = null,
) : MviState

sealed interface WorkoutsEffect : MviEffect {
    data class NavigateToWorkoutDetail(val workoutId: String) : WorkoutsEffect
}
