package com.example.homeworkout.presentation.navigation

import androidx.annotation.StringRes
import com.example.homeworkout.R

object Routes {
    const val MAIN = "main"
    const val SETTINGS = "settings"
    const val WORKOUT_DETAIL = "workout_detail/{workoutId}"
    const val ACTIVE_WORKOUT = "active_workout/{workoutId}"

    fun workoutDetail(workoutId: String) = "workout_detail/$workoutId"
    fun activeWorkout(workoutId: String) = "active_workout/$workoutId"
}

enum class BottomNavItem(
    val route: String,
    @param:StringRes val labelRes: Int,
) {
    HOME("home", R.string.nav_home),
    WORKOUTS("workouts", R.string.nav_workouts),
    REPORT("report", R.string.nav_report),
    PROFILE("profile", R.string.nav_profile),
}
