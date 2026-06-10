package com.example.homeworkout.presentation.workouts

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.homeworkout.R
import com.example.homeworkout.domain.model.Difficulty
import com.example.homeworkout.domain.model.WorkoutCategory
import com.example.homeworkout.presentation.components.CategoryFilterChip
import com.example.homeworkout.presentation.components.WorkoutListCard

@Composable
fun WorkoutsScreen(
    onWorkoutClick: (String) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: WorkoutsViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(viewModel) {
        viewModel.effect.collect { effect ->
            when (effect) {
                is WorkoutsEffect.NavigateToWorkoutDetail -> onWorkoutClick(effect.workoutId)
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
                verticalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                item {
                    Text(
                        text = stringResource(R.string.workouts_title),
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold,
                    )
                }

                item {
                    Text(
                        text = stringResource(R.string.workouts_body_part),
                        style = MaterialTheme.typography.titleSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                    LazyRow(
                        modifier = Modifier.padding(top = 8.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                    ) {
                        item {
                            CategoryFilterChip(
                                label = stringResource(R.string.filter_all),
                                selected = state.selectedCategory == null,
                                onClick = { viewModel.onIntent(WorkoutsIntent.CategorySelected(null)) },
                            )
                        }
                        items(WorkoutCategory.entries) { category ->
                            CategoryFilterChip(
                                label = category.displayName,
                                selected = state.selectedCategory == category,
                                onClick = {
                                    viewModel.onIntent(WorkoutsIntent.CategorySelected(category))
                                },
                            )
                        }
                    }
                }

                item {
                    Text(
                        text = stringResource(R.string.workouts_difficulty),
                        style = MaterialTheme.typography.titleSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                    LazyRow(
                        modifier = Modifier.padding(top = 8.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                    ) {
                        item {
                            CategoryFilterChip(
                                label = stringResource(R.string.filter_all),
                                selected = state.selectedDifficulty == null,
                                onClick = { viewModel.onIntent(WorkoutsIntent.DifficultySelected(null)) },
                            )
                        }
                        items(Difficulty.entries) { difficulty ->
                            CategoryFilterChip(
                                label = difficulty.displayName,
                                selected = state.selectedDifficulty == difficulty,
                                onClick = {
                                    viewModel.onIntent(WorkoutsIntent.DifficultySelected(difficulty))
                                },
                            )
                        }
                    }
                }

                item {
                    Text(
                        text = stringResource(R.string.workouts_count, state.filteredWorkouts.size),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }

                items(state.filteredWorkouts, key = { it.id }) { workout ->
                    WorkoutListCard(
                        workout = workout,
                        onClick = { viewModel.onIntent(WorkoutsIntent.WorkoutClicked(workout.id)) },
                        modifier = Modifier.fillMaxWidth(),
                    )
                }
            }
        }
    }
}
