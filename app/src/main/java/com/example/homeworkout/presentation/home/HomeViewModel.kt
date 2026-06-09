package com.example.homeworkout.presentation.home

import androidx.lifecycle.viewModelScope
import com.example.homeworkout.domain.model.WorkoutCategory
import com.example.homeworkout.domain.usecase.GetFeaturedWorkoutUseCase
import com.example.homeworkout.domain.usecase.GetUserProgressUseCase
import com.example.homeworkout.domain.usecase.GetWorkoutsUseCase
import com.example.homeworkout.presentation.mvi.MviViewModel
import kotlinx.coroutines.launch

class HomeViewModel(
    private val getFeaturedWorkoutUseCase: GetFeaturedWorkoutUseCase,
    private val getWorkoutsUseCase: GetWorkoutsUseCase,
    private val getUserProgressUseCase: GetUserProgressUseCase,
) : MviViewModel<HomeIntent, HomeState, HomeEffect>(HomeState()) {

    init {
        onIntent(HomeIntent.Load)
    }

    override fun onIntent(intent: HomeIntent) {
        when (intent) {
            HomeIntent.Load -> loadHome()
            is HomeIntent.CategorySelected -> filterByCategory(intent.category)
            is HomeIntent.WorkoutClicked -> sendEffect { HomeEffect.NavigateToWorkoutDetail(intent.workoutId) }
        }
    }

    private fun loadHome() {
        viewModelScope.launch {
            setState { copy(isLoading = true, errorMessage = null) }
            runCatching {
                val featured = getFeaturedWorkoutUseCase()
                val workouts = getWorkoutsUseCase()
                val progress = getUserProgressUseCase()
                setState {
                    copy(
                        isLoading = false,
                        featuredWorkout = featured,
                        workouts = workouts,
                        filteredWorkouts = workouts,
                        progress = progress,
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

    private fun filterByCategory(category: WorkoutCategory?) {
        setState {
            val filtered = if (category == null) {
                workouts
            } else {
                workouts.filter { it.category == category }
            }
            copy(selectedCategory = category, filteredWorkouts = filtered)
        }
    }
}
