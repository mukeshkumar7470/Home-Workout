package com.example.homeworkout.data.repository

import com.example.homeworkout.data.local.RoomProgressDataSource
import com.example.homeworkout.data.local.WorkoutAssetDataSource
import com.example.homeworkout.data.mapper.toDomain
import com.example.homeworkout.domain.model.UserProgress
import com.example.homeworkout.domain.model.Workout
import com.example.homeworkout.domain.model.WorkoutCategory
import com.example.homeworkout.domain.repository.WorkoutRepository

class WorkoutRepositoryImpl(
    private val assetDataSource: WorkoutAssetDataSource,
    private val progressDataSource: RoomProgressDataSource,
) : WorkoutRepository {

    private val workouts: List<Workout> by lazy {
        assetDataSource.catalog.workouts.map { it.toDomain() }
    }

    override suspend fun getWorkouts(): List<Workout> = workouts

    override suspend fun getWorkoutById(id: String): Workout? =
        workouts.firstOrNull { it.id == id }

    override suspend fun getFeaturedWorkout(): Workout? =
        workouts.firstOrNull { it.isFeatured } ?: workouts.firstOrNull()

    override suspend fun getWorkoutsByCategory(category: WorkoutCategory): List<Workout> =
        workouts.filter { it.category == category }

    override suspend fun getUserProgress(): UserProgress =
        progressDataSource.getProgress()

    override suspend fun saveWorkoutCompletion(workout: Workout) {
        progressDataSource.recordCompletion(workout)
    }
}
