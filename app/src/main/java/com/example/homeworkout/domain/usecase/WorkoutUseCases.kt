package com.example.homeworkout.domain.usecase

import com.example.homeworkout.domain.model.UserProgress
import com.example.homeworkout.domain.model.Workout
import com.example.homeworkout.domain.model.WorkoutCategory
import com.example.homeworkout.domain.repository.WorkoutRepository

class GetWorkoutsUseCase(
    private val repository: WorkoutRepository,
) {
    suspend operator fun invoke(): List<Workout> = repository.getWorkouts()
}

class GetWorkoutByIdUseCase(
    private val repository: WorkoutRepository,
) {
    suspend operator fun invoke(id: String): Workout? = repository.getWorkoutById(id)
}

class GetFeaturedWorkoutUseCase(
    private val repository: WorkoutRepository,
) {
    suspend operator fun invoke(): Workout? = repository.getFeaturedWorkout()
}

class GetWorkoutsByCategoryUseCase(
    private val repository: WorkoutRepository,
) {
    suspend operator fun invoke(category: WorkoutCategory): List<Workout> =
        repository.getWorkoutsByCategory(category)
}

class GetUserProgressUseCase(
    private val repository: WorkoutRepository,
) {
    suspend operator fun invoke(): UserProgress = repository.getUserProgress()
}

class SaveWorkoutCompletionUseCase(
    private val repository: WorkoutRepository,
) {
    suspend operator fun invoke(workout: Workout) = repository.saveWorkoutCompletion(workout)
}
