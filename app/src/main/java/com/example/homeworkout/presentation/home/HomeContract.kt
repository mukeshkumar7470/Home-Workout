package com.example.homeworkout.presentation.home

import com.example.homeworkout.domain.model.UserProgress
import com.example.homeworkout.domain.model.Workout
import com.example.homeworkout.domain.model.WorkoutCategory
import com.example.homeworkout.presentation.mvi.MviEffect
import com.example.homeworkout.presentation.mvi.MviIntent
import com.example.homeworkout.presentation.mvi.MviState

sealed interface HomeIntent : MviIntent {
    data object Load : HomeIntent
    data class CategorySelected(val category: WorkoutCategory?) : HomeIntent
    data class WorkoutClicked(val workoutId: String) : HomeIntent
}

data class HomeState(
    val isLoading: Boolean = true,
    val featuredWorkout: Workout? = null,
    val workouts: List<Workout> = emptyList(),
    val filteredWorkouts: List<Workout> = emptyList(),
    val selectedCategory: WorkoutCategory? = null,
    val progress: UserProgress = UserProgress(),
    val errorMessage: String? = null,
) : MviState

sealed interface HomeEffect : MviEffect {
    data class NavigateToWorkoutDetail(val workoutId: String) : HomeEffect
}
