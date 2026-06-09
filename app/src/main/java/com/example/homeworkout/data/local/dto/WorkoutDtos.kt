package com.example.homeworkout.data.local.dto

import kotlinx.serialization.Serializable

@Serializable
data class WorkoutCatalogDto(
    val workouts: List<WorkoutDto> = emptyList(),
)

@Serializable
data class WorkoutDto(
    val id: String,
    val name: String,
    val description: String,
    val category: String,
    val difficulty: String,
    val durationMinutes: Int,
    val calories: Int,
    val accentColor: String,
    val isFeatured: Boolean = false,
    val exercises: List<ExerciseDto> = emptyList(),
)

@Serializable
data class ExerciseDto(
    val id: String,
    val name: String,
    val durationSeconds: Int,
    val restSeconds: Int,
    val instructions: String,
    val muscleGroup: String,
    val caloriesPerMinute: Int = 8,
)

@Serializable
data class UserProgressDto(
    val totalWorkouts: Int = 0,
    val totalMinutes: Int = 0,
    val totalCalories: Int = 0,
    val currentStreak: Int = 0,
    val bestStreak: Int = 0,
    val history: List<WorkoutHistoryDto> = emptyList(),
)

@Serializable
data class WorkoutHistoryDto(
    val workoutId: String,
    val workoutName: String,
    val completedAtMillis: Long,
    val durationMinutes: Int,
    val caloriesBurned: Int,
)
