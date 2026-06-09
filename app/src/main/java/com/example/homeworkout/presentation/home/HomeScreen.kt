package com.example.homeworkout.presentation.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.homeworkout.di.appViewModel
import com.example.homeworkout.domain.model.WorkoutCategory
import com.example.homeworkout.presentation.components.CategoryFilterChip
import com.example.homeworkout.presentation.components.FeaturedWorkoutCard
import com.example.homeworkout.presentation.components.StatPill
import com.example.homeworkout.presentation.components.WorkoutListCard

@Composable
fun HomeScreen(
    onWorkoutClick: (String) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = appViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(viewModel) {
        viewModel.effect.collect { effect ->
            when (effect) {
                is HomeEffect.NavigateToWorkoutDetail -> onWorkoutClick(effect.workoutId)
            }
        }
    }

    when {
        state.isLoading -> {
            Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
            }
        }

        state.errorMessage != null -> {
            Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(text = state.errorMessage ?: "Error")
            }
        }

        else -> {
            LazyColumn(
                modifier = modifier.fillMaxSize(),
                contentPadding = PaddingValues(horizontal = 20.dp, vertical = 16.dp),
                verticalArrangement = Arrangement.spacedBy(20.dp),
            ) {
                item {
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Text(
                            text = "Home Workout",
                            style = MaterialTheme.typography.headlineMedium,
                            fontWeight = FontWeight.Bold,
                        )
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            StatPill(
                                icon = Icons.Default.LocalFireDepartment,
                                label = "${state.progress.currentStreak} day streak",
                            )
                            StatPill(
                                icon = Icons.Default.LocalFireDepartment,
                                label = "${state.progress.totalWorkouts} workouts",
                            )
                        }
                    }
                }

                state.featuredWorkout?.let { featured ->
                    item {
                        FeaturedWorkoutCard(
                            workout = featured,
                            onClick = { viewModel.onIntent(HomeIntent.WorkoutClicked(featured.id)) },
                        )
                    }
                }

                item {
                    Text(
                        text = "Categories",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold,
                    )
                }

                item {
                    LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        item {
                            CategoryFilterChip(
                                label = "All",
                                selected = state.selectedCategory == null,
                                onClick = { viewModel.onIntent(HomeIntent.CategorySelected(null)) },
                            )
                        }
                        items(WorkoutCategory.entries.filter {
                            it != WorkoutCategory.WARM_UP && it != WorkoutCategory.STRETCH
                        }) { category ->
                            CategoryFilterChip(
                                label = category.displayName,
                                selected = state.selectedCategory == category,
                                onClick = {
                                    viewModel.onIntent(HomeIntent.CategorySelected(category))
                                },
                            )
                        }
                    }
                }

                item {
                    Text(
                        text = "Popular Workouts",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold,
                    )
                }

                items(state.filteredWorkouts, key = { it.id }) { workout ->
                    WorkoutListCard(
                        workout = workout,
                        onClick = { viewModel.onIntent(HomeIntent.WorkoutClicked(workout.id)) },
                        modifier = Modifier.fillMaxWidth(),
                    )
                }
            }
        }
    }
}
