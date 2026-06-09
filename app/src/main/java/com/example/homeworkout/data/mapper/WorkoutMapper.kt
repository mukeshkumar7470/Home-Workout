package com.example.homeworkout.data.mapper

import com.example.homeworkout.data.local.dto.ExerciseDto
import com.example.homeworkout.data.local.dto.UserProgressDto
import com.example.homeworkout.data.local.dto.WorkoutDto
import com.example.homeworkout.data.local.dto.WorkoutHistoryDto
import com.example.homeworkout.domain.model.Difficulty
import com.example.homeworkout.domain.model.Exercise
import com.example.homeworkout.domain.model.MuscleGroup
import com.example.homeworkout.domain.model.UserProgress
import com.example.homeworkout.domain.model.Workout
import com.example.homeworkout.domain.model.WorkoutCategory
import com.example.homeworkout.domain.model.WorkoutHistoryEntry

fun WorkoutDto.toDomain(): Workout = Workout(
    id = id,
    name = name,
    description = description,
    category = WorkoutCategory.fromRaw(category),
    difficulty = Difficulty.fromRaw(difficulty),
    durationMinutes = durationMinutes,
    calories = calories,
    accentColor = accentColor,
    isFeatured = isFeatured,
    exercises = exercises.map { it.toDomain() },
)

fun ExerciseDto.toDomain(): Exercise = Exercise(
    id = id,
    name = name,
    durationSeconds = durationSeconds,
    restSeconds = restSeconds,
    instructions = instructions,
    muscleGroup = MuscleGroup.fromRaw(muscleGroup),
    caloriesPerMinute = caloriesPerMinute,
)

fun UserProgressDto.toDomain(): UserProgress = UserProgress(
    totalWorkouts = totalWorkouts,
    totalMinutes = totalMinutes,
    totalCalories = totalCalories,
    currentStreak = currentStreak,
    bestStreak = bestStreak,
    history = history.map { it.toDomain() },
)

fun UserProgress.toDto(): UserProgressDto = UserProgressDto(
    totalWorkouts = totalWorkouts,
    totalMinutes = totalMinutes,
    totalCalories = totalCalories,
    currentStreak = currentStreak,
    bestStreak = bestStreak,
    history = history.map { it.toDto() },
)

fun WorkoutHistoryEntry.toDto(): WorkoutHistoryDto = WorkoutHistoryDto(
    workoutId = workoutId,
    workoutName = workoutName,
    completedAtMillis = completedAtMillis,
    durationMinutes = durationMinutes,
    caloriesBurned = caloriesBurned,
)

fun WorkoutHistoryDto.toDomain(): WorkoutHistoryEntry = WorkoutHistoryEntry(
    workoutId = workoutId,
    workoutName = workoutName,
    completedAtMillis = completedAtMillis,
    durationMinutes = durationMinutes,
    caloriesBurned = caloriesBurned,
)
