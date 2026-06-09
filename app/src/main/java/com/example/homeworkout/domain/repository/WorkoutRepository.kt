package com.example.homeworkout.domain.repository

import com.example.homeworkout.domain.model.UserProgress
import com.example.homeworkout.domain.model.Workout
import com.example.homeworkout.domain.model.WorkoutCategory

interface WorkoutRepository {
    suspend fun getWorkouts(): List<Workout>
    suspend fun getWorkoutById(id: String): Workout?
    suspend fun getFeaturedWorkout(): Workout?
    suspend fun getWorkoutsByCategory(category: WorkoutCategory): List<Workout>
    suspend fun getUserProgress(): UserProgress
    suspend fun saveWorkoutCompletion(workout: Workout)
}
