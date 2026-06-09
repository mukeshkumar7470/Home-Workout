package com.example.homeworkout.data.local

import android.content.Context
import com.example.homeworkout.data.local.dto.UserProgressDto
import com.example.homeworkout.data.mapper.toDomain
import com.example.homeworkout.data.mapper.toDto
import com.example.homeworkout.domain.model.UserProgress
import com.example.homeworkout.domain.model.Workout
import com.example.homeworkout.domain.model.WorkoutHistoryEntry
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.util.Calendar

class ProgressLocalDataSource(
    context: Context,
    private val json: Json = Json { ignoreUnknownKeys = true },
) {
    private val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    fun getProgress(): UserProgress {
        val raw = prefs.getString(KEY_PROGRESS, null) ?: return UserProgress()
        return runCatching {
            json.decodeFromString<UserProgressDto>(raw).toDomain()
        }.getOrDefault(UserProgress())
    }

    fun saveProgress(progress: UserProgress) {
        prefs.edit()
            .putString(KEY_PROGRESS, json.encodeToString(progress.toDto()))
            .apply()
    }

    fun recordCompletion(workout: Workout): UserProgress {
        val current = getProgress()
        val now = System.currentTimeMillis()
        val entry = WorkoutHistoryEntry(
            workoutId = workout.id,
            workoutName = workout.name,
            completedAtMillis = now,
            durationMinutes = workout.durationMinutes,
            caloriesBurned = workout.calories,
        )
        val today = startOfDay(now)
        val hadWorkoutToday = current.history.any { startOfDay(it.completedAtMillis) == today }
        val hadWorkoutYesterday = current.history.any {
            startOfDay(it.completedAtMillis) == today - DAY_MILLIS
        }
        val streak = when {
            hadWorkoutToday -> current.currentStreak.coerceAtLeast(1)
            hadWorkoutYesterday -> current.currentStreak + 1
            else -> 1
        }
        val updatedHistory = listOf(entry) + current.history.take(MAX_HISTORY - 1)
        val updated = current.copy(
            totalWorkouts = current.totalWorkouts + 1,
            totalMinutes = current.totalMinutes + workout.durationMinutes,
            totalCalories = current.totalCalories + workout.calories,
            currentStreak = streak,
            bestStreak = maxOf(current.bestStreak, streak),
            history = updatedHistory,
        )
        saveProgress(updated)
        return updated
    }

    private fun startOfDay(millis: Long): Long {
        val calendar = Calendar.getInstance().apply {
            timeInMillis = millis
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }
        return calendar.timeInMillis
    }

    companion object {
        private const val PREFS_NAME = "home_workout_progress"
        private const val KEY_PROGRESS = "user_progress"
        private const val MAX_HISTORY = 30
        private const val DAY_MILLIS = 24 * 60 * 60 * 1000L
    }
}
