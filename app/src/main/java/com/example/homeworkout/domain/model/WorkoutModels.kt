package com.example.homeworkout.domain.model

enum class WorkoutCategory(val displayName: String) {
    FULL_BODY("Full Body"),
    ABS("Abs"),
    CHEST("Chest"),
    LEGS("Legs"),
    ARMS("Arms"),
    BUTT("Butt"),
    HIIT("HIIT"),
    WARM_UP("Warm Up"),
    STRETCH("Stretch");

    companion object {
        fun fromRaw(value: String): WorkoutCategory =
            entries.firstOrNull { it.name.equals(value, ignoreCase = true) } ?: FULL_BODY
    }
}

enum class Difficulty(val displayName: String) {
    BEGINNER("Beginner"),
    INTERMEDIATE("Intermediate"),
    ADVANCED("Advanced");

    companion object {
        fun fromRaw(value: String): Difficulty =
            entries.firstOrNull { it.name.equals(value, ignoreCase = true) } ?: BEGINNER
    }
}

enum class MuscleGroup(val displayName: String) {
    FULL_BODY("Full Body"),
    ABS("Abs"),
    CHEST("Chest"),
    LEGS("Legs"),
    ARMS("Arms"),
    BUTT("Butt"),
    BACK("Back"),
    SHOULDERS("Shoulders");

    companion object {
        fun fromRaw(value: String): MuscleGroup =
            entries.firstOrNull { it.name.equals(value, ignoreCase = true) } ?: FULL_BODY
    }
}

data class Exercise(
    val id: String,
    val name: String,
    val durationSeconds: Int,
    val restSeconds: Int,
    val instructions: String,
    val muscleGroup: MuscleGroup,
    val caloriesPerMinute: Int = 8,
)

data class Workout(
    val id: String,
    val name: String,
    val description: String,
    val category: WorkoutCategory,
    val difficulty: Difficulty,
    val durationMinutes: Int,
    val calories: Int,
    val accentColor: String,
    val isFeatured: Boolean = false,
    val exercises: List<Exercise>,
)

data class WorkoutHistoryEntry(
    val workoutId: String,
    val workoutName: String,
    val completedAtMillis: Long,
    val durationMinutes: Int,
    val caloriesBurned: Int,
)

data class UserProgress(
    val totalWorkouts: Int = 0,
    val totalMinutes: Int = 0,
    val totalCalories: Int = 0,
    val currentStreak: Int = 0,
    val bestStreak: Int = 0,
    val history: List<WorkoutHistoryEntry> = emptyList(),
)

data class ActiveWorkoutPhase(
    val exerciseIndex: Int,
    val remainingSeconds: Int,
    val isRestPhase: Boolean,
    val isPaused: Boolean,
)

data class ActiveWorkoutState(
    val workout: Workout,
    val phase: ActiveWorkoutPhase,
    val completedExercises: Int,
) {
    val currentExercise: Exercise?
        get() = workout.exercises.getOrNull(phase.exerciseIndex)

    val nextExercise: Exercise?
        get() = workout.exercises.getOrNull(phase.exerciseIndex + 1)

    val progress: Float
        get() {
            val total = workout.exercises.size * 2
            val done = completedExercises * 2 + if (phase.isRestPhase) 1 else 0
            return if (total == 0) 0f else done.toFloat() / total
        }

    val isFinished: Boolean
        get() = phase.exerciseIndex >= workout.exercises.size
}
