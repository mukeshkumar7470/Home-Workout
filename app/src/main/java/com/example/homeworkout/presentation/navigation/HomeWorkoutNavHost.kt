package com.example.homeworkout.presentation.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.homeworkout.presentation.activeworkout.ActiveWorkoutScreen
import com.example.homeworkout.presentation.home.HomeScreen
import com.example.homeworkout.presentation.profile.ProfileScreen
import com.example.homeworkout.presentation.report.ReportScreen
import com.example.homeworkout.presentation.settings.SettingsScreen
import com.example.homeworkout.presentation.workoutdetail.WorkoutDetailScreen
import com.example.homeworkout.presentation.workouts.WorkoutsScreen

@Composable
fun HomeWorkoutNavHost(
    modifier: Modifier = Modifier,
) {
    val navController = rememberNavController()
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = backStackEntry?.destination?.route
    val bottomNavRoutes = BottomNavItem.entries.map { it.route }
    val showBottomBar = currentRoute in bottomNavRoutes

    Scaffold(
        modifier = modifier,
        bottomBar = {
            if (showBottomBar) {
                NavigationBar {
                    BottomNavItem.entries.forEach { item ->
                        val selected = currentRoute == item.route
                        val label = stringResource(item.labelRes)
                        NavigationBarItem(
                            selected = selected,
                            onClick = {
                                navController.navigate(item.route) {
                                    popUpTo(navController.graph.findStartDestination().id) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            },
                            icon = {
                                Icon(
                                    imageVector = when (item) {
                                        BottomNavItem.HOME -> Icons.Default.Home
                                        BottomNavItem.WORKOUTS -> Icons.Default.FitnessCenter
                                        BottomNavItem.REPORT -> Icons.Default.BarChart
                                        BottomNavItem.PROFILE -> Icons.Default.Person
                                    },
                                    contentDescription = label,
                                )
                            },
                            label = { Text(label) },
                        )
                    }
                }
            }
        },
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = BottomNavItem.HOME.route,
            modifier = Modifier.padding(innerPadding),
        ) {
            composable(BottomNavItem.HOME.route) {
                HomeScreen(
                    onWorkoutClick = { workoutId ->
                        navController.navigate(Routes.workoutDetail(workoutId))
                    },
                )
            }

            composable(BottomNavItem.WORKOUTS.route) {
                WorkoutsScreen(
                    onWorkoutClick = { workoutId ->
                        navController.navigate(Routes.workoutDetail(workoutId))
                    },
                )
            }

            composable(BottomNavItem.REPORT.route) {
                ReportScreen()
            }

            composable(BottomNavItem.PROFILE.route) {
                ProfileScreen(
                    onNavigateToSettings = { navController.navigate(Routes.SETTINGS) },
                )
            }

            composable(Routes.SETTINGS) {
                SettingsScreen(onBack = { navController.popBackStack() })
            }

            composable(
                route = Routes.WORKOUT_DETAIL,
                arguments = listOf(navArgument("workoutId") { type = NavType.StringType }),
            ) { entry ->
                val workoutId = entry.arguments?.getString("workoutId").orEmpty()
                WorkoutDetailScreen(
                    workoutId = workoutId,
                    onBack = { navController.popBackStack() },
                    onStartWorkout = { id ->
                        navController.navigate(Routes.activeWorkout(id))
                    },
                )
            }

            composable(
                route = Routes.ACTIVE_WORKOUT,
                arguments = listOf(navArgument("workoutId") { type = NavType.StringType }),
            ) { entry ->
                val workoutId = entry.arguments?.getString("workoutId").orEmpty()
                ActiveWorkoutScreen(
                    workoutId = workoutId,
                    onBack = { navController.popBackStack() },
                )
            }
        }
    }
}
