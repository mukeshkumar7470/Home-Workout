package com.example.homeworkout.presentation.navigation

object Routes {
    const val MAIN = "main"
    const val WORKOUT_DETAIL = "workout_detail/{workoutId}"
    const val ACTIVE_WORKOUT = "active_workout/{workoutId}"

    fun workoutDetail(workoutId: String) = "workout_detail/$workoutId"
    fun activeWorkout(workoutId: String) = "active_workout/$workoutId"
}

enum class BottomNavItem(
    val route: String,
    val label: String,
) {
    HOME("home", "Home"),
    WORKOUTS("workouts", "Workouts"),
    REPORT("report", "Report"),
    PROFILE("profile", "Me"),
}
