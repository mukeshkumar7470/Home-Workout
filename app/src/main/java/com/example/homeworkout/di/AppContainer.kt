package com.example.homeworkout.di

import android.content.Context
import com.example.homeworkout.data.local.ProgressLocalDataSource
import com.example.homeworkout.data.local.WorkoutAssetDataSource
import com.example.homeworkout.data.repository.WorkoutRepositoryImpl
import com.example.homeworkout.domain.repository.WorkoutRepository
import com.example.homeworkout.domain.usecase.GetFeaturedWorkoutUseCase
import com.example.homeworkout.domain.usecase.GetUserProgressUseCase
import com.example.homeworkout.domain.usecase.GetWorkoutByIdUseCase
import com.example.homeworkout.domain.usecase.GetWorkoutsByCategoryUseCase
import com.example.homeworkout.domain.usecase.GetWorkoutsUseCase
import com.example.homeworkout.domain.usecase.SaveWorkoutCompletionUseCase

class AppContainer(context: Context) {

    private val workoutAssetDataSource = WorkoutAssetDataSource(context.applicationContext)
    private val progressLocalDataSource = ProgressLocalDataSource(context.applicationContext)

    val workoutRepository: WorkoutRepository = WorkoutRepositoryImpl(
        assetDataSource = workoutAssetDataSource,
        progressDataSource = progressLocalDataSource,
    )

    val getWorkoutsUseCase = GetWorkoutsUseCase(workoutRepository)
    val getWorkoutByIdUseCase = GetWorkoutByIdUseCase(workoutRepository)
    val getFeaturedWorkoutUseCase = GetFeaturedWorkoutUseCase(workoutRepository)
    val getWorkoutsByCategoryUseCase = GetWorkoutsByCategoryUseCase(workoutRepository)
    val getUserProgressUseCase = GetUserProgressUseCase(workoutRepository)
    val saveWorkoutCompletionUseCase = SaveWorkoutCompletionUseCase(workoutRepository)
}
