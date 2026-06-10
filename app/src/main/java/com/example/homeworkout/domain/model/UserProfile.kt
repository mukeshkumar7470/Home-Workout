package com.example.homeworkout.domain.model

enum class FitnessGoal(val displayName: String) {
    LOSE_WEIGHT("Lose Weight"),
    BUILD_MUSCLE("Build Muscle"),
    STAY_ACTIVE("Stay Active"),
    IMPROVE_ENDURANCE("Improve Endurance");

    companion object {
        fun fromRaw(value: String): FitnessGoal =
            entries.firstOrNull { it.name.equals(value, ignoreCase = true) } ?: STAY_ACTIVE
    }
}

enum class FitnessLevel(val displayName: String) {
    BEGINNER("Beginner"),
    INTERMEDIATE("Intermediate"),
    ADVANCED("Advanced");

    companion object {
        fun fromRaw(value: String): FitnessLevel =
            entries.firstOrNull { it.name.equals(value, ignoreCase = true) } ?: BEGINNER
    }
}

data class UserProfile(
    val age: Int? = null,
    val weightKg: Float? = null,
    val fitnessGoal: FitnessGoal? = null,
    val fitnessLevel: FitnessLevel? = null,
    val onboardingCompleted: Boolean = false,
)
