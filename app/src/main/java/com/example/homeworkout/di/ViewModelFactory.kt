package com.example.homeworkout.di

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.homeworkout.presentation.activeworkout.ActiveWorkoutViewModel
import com.example.homeworkout.presentation.home.HomeViewModel
import com.example.homeworkout.presentation.profile.ProfileViewModel
import com.example.homeworkout.presentation.report.ReportViewModel
import com.example.homeworkout.presentation.workoutdetail.WorkoutDetailViewModel
import com.example.homeworkout.presentation.workouts.WorkoutsViewModel

val LocalAppContainer = staticCompositionLocalOf<AppContainer> {
    error("AppContainer not provided")
}

@Composable
fun ProvideAppContainer(content: @Composable () -> Unit) {
    val context = LocalContext.current
    val container = AppContainer(context.applicationContext)
    CompositionLocalProvider(LocalAppContainer provides container) {
        content()
    }
}

class AppViewModelFactory(
    private val container: AppContainer,
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(HomeViewModel::class.java) ->
                HomeViewModel(
                    getFeaturedWorkoutUseCase = container.getFeaturedWorkoutUseCase,
                    getWorkoutsUseCase = container.getWorkoutsUseCase,
                    getUserProgressUseCase = container.getUserProgressUseCase,
                ) as T

            modelClass.isAssignableFrom(WorkoutsViewModel::class.java) ->
                WorkoutsViewModel(
                    getWorkoutsUseCase = container.getWorkoutsUseCase,
                ) as T

            modelClass.isAssignableFrom(WorkoutDetailViewModel::class.java) ->
                WorkoutDetailViewModel(
                    getWorkoutByIdUseCase = container.getWorkoutByIdUseCase,
                ) as T

            modelClass.isAssignableFrom(ActiveWorkoutViewModel::class.java) ->
                ActiveWorkoutViewModel(
                    getWorkoutByIdUseCase = container.getWorkoutByIdUseCase,
                    saveWorkoutCompletionUseCase = container.saveWorkoutCompletionUseCase,
                ) as T

            modelClass.isAssignableFrom(ReportViewModel::class.java) ->
                ReportViewModel(
                    getUserProgressUseCase = container.getUserProgressUseCase,
                ) as T

            modelClass.isAssignableFrom(ProfileViewModel::class.java) ->
                ProfileViewModel(
                    getUserProgressUseCase = container.getUserProgressUseCase,
                ) as T

            else -> throw IllegalArgumentException("Unknown ViewModel: ${modelClass.name}")
        }
    }
}

@Composable
inline fun <reified VM : ViewModel> appViewModel(): VM {
    val container = LocalAppContainer.current
    return viewModel(factory = AppViewModelFactory(container))
}

@Composable
inline fun <reified VM : ViewModel> appViewModel(key: String): VM {
    val container = LocalAppContainer.current
    return viewModel(key = key, factory = AppViewModelFactory(container))
}
