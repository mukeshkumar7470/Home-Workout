package com.example.homeworkout.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "workout_history")
data class WorkoutHistoryEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val workoutId: String,
    val workoutName: String,
    val completedAtMillis: Long,
    val durationMinutes: Int,
    val caloriesBurned: Int,
)

@Entity(tableName = "user_stats")
data class UserStatsEntity(
    @PrimaryKey val id: Int = SINGLETON_ID,
    val totalWorkouts: Int = 0,
    val totalMinutes: Int = 0,
    val totalCalories: Int = 0,
    val currentStreak: Int = 0,
    val bestStreak: Int = 0,
) {
    companion object {
        const val SINGLETON_ID = 1
    }
}

@Entity(tableName = "user_profile")
data class UserProfileEntity(
    @PrimaryKey val id: Int = SINGLETON_ID,
    val age: Int? = null,
    val weightKg: Float? = null,
    val fitnessGoal: String? = null,
    val fitnessLevel: String? = null,
    val onboardingCompleted: Boolean = false,
) {
    companion object {
        const val SINGLETON_ID = 1
    }
}
