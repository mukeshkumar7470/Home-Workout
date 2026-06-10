package com.example.homeworkout.presentation.components

object ExerciseAnimationResolver {
    private const val GENERIC = "lottie/generic_workout.json"

    fun resolveAsset(exerciseId: String, isRestPhase: Boolean = false): String {
        if (isRestPhase) return GENERIC
        return when {
            exerciseId.contains("push", ignoreCase = true) -> GENERIC
            exerciseId.contains("squat", ignoreCase = true) -> GENERIC
            exerciseId.contains("plank", ignoreCase = true) -> GENERIC
            exerciseId.contains("jump", ignoreCase = true) -> GENERIC
            exerciseId.contains("lunge", ignoreCase = true) -> GENERIC
            exerciseId.contains("crunch", ignoreCase = true) -> GENERIC
            exerciseId.contains("burpee", ignoreCase = true) -> GENERIC
            else -> GENERIC
        }
    }
}
