package com.example.homeworkout.data.local

import com.example.homeworkout.data.local.dao.UserStatsDao
import com.example.homeworkout.data.local.dao.WorkoutHistoryDao
import com.example.homeworkout.data.local.entity.UserStatsEntity
import com.example.homeworkout.data.local.entity.WorkoutHistoryEntity
import com.example.homeworkout.domain.model.UserProgress
import com.example.homeworkout.domain.model.Workout
import com.example.homeworkout.domain.model.WorkoutHistoryEntry
import java.util.Calendar

class RoomProgressDataSource(
    private val historyDao: WorkoutHistoryDao,
    private val statsDao: UserStatsDao,
) {

    suspend fun getProgress(): UserProgress {
        ensureStatsExist()
        val stats = statsDao.getStats() ?: UserStatsEntity()
        val history = historyDao.getRecentHistory(MAX_HISTORY).map { it.toDomain() }
        return UserProgress(
            totalWorkouts = stats.totalWorkouts,
            totalMinutes = stats.totalMinutes,
            totalCalories = stats.totalCalories,
            currentStreak = stats.currentStreak,
            bestStreak = stats.bestStreak,
            history = history,
        )
    }

    suspend fun recordCompletion(workout: Workout): UserProgress {
        ensureStatsExist()
        val current = getProgress()
        val now = System.currentTimeMillis()
        historyDao.insert(
            WorkoutHistoryEntity(
                workoutId = workout.id,
                workoutName = workout.name,
                completedAtMillis = now,
                durationMinutes = workout.durationMinutes,
                caloriesBurned = workout.calories,
            ),
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
        val updatedStats = UserStatsEntity(
            totalWorkouts = current.totalWorkouts + 1,
            totalMinutes = current.totalMinutes + workout.durationMinutes,
            totalCalories = current.totalCalories + workout.calories,
            currentStreak = streak,
            bestStreak = maxOf(current.bestStreak, streak),
        )
        statsDao.upsert(updatedStats)
        return getProgress()
    }

    private suspend fun ensureStatsExist() {
        if (statsDao.getStats() == null) {
            statsDao.upsert(UserStatsEntity())
        }
    }

    private fun WorkoutHistoryEntity.toDomain(): WorkoutHistoryEntry = WorkoutHistoryEntry(
        workoutId = workoutId,
        workoutName = workoutName,
        completedAtMillis = completedAtMillis,
        durationMinutes = durationMinutes,
        caloriesBurned = caloriesBurned,
    )

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
        private const val MAX_HISTORY = 30
        private const val DAY_MILLIS = 24 * 60 * 60 * 1000L
    }
}
