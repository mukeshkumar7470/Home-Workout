package com.example.homeworkout.presentation.activeworkout

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.SkipNext
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.homeworkout.di.appViewModel
import com.example.homeworkout.presentation.components.parseAccentColor
import kotlinx.coroutines.delay

@Composable
fun ActiveWorkoutScreen(
    workoutId: String,
    onBack: () -> Unit,
    viewModel: ActiveWorkoutViewModel = appViewModel(key = "active_$workoutId"),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    var completionDialog by remember { mutableStateOf<Pair<String, Int>?>(null) }

    LaunchedEffect(workoutId) {
        viewModel.onIntent(ActiveWorkoutIntent.Load(workoutId))
    }

    LaunchedEffect(viewModel) {
        viewModel.effect.collect { effect ->
            when (effect) {
                ActiveWorkoutEffect.NavigateBack -> onBack()
                is ActiveWorkoutEffect.ShowCompletion ->
                    completionDialog = effect.workoutName to effect.calories
            }
        }
    }

    LaunchedEffect(Unit) {
        while (true) {
            delay(1000)
            viewModel.onIntent(ActiveWorkoutIntent.Tick)
        }
    }

    completionDialog?.let { (name, calories) ->
        AlertDialog(
            onDismissRequest = {
                completionDialog = null
                onBack()
            },
            title = { Text("Workout Complete!") },
            text = {
                Text("Great job finishing $name! You burned approximately $calories kcal.")
            },
            confirmButton = {
                TextButton(onClick = {
                    completionDialog = null
                    onBack()
                }) {
                    Text("Done")
                }
            },
        )
    }

    when {
        state.isLoading -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
            }
        }

        state.errorMessage != null -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(text = state.errorMessage ?: "Error")
            }
        }

        state.activeWorkout != null -> {
            val active = state.activeWorkout!!
            val workout = active.workout
            val accent = parseAccentColor(workout.accentColor)
            val phaseLabel = if (active.phase.isRestPhase) "REST" else "WORK"
            val currentName = if (active.phase.isRestPhase) {
                "Rest before ${active.nextExercise?.name ?: "finish"}"
            } else {
                active.currentExercise?.name ?: "Workout"
            }

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.background)
                    .padding(20.dp),
                verticalArrangement = Arrangement.SpaceBetween,
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(
                        text = workout.name,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold,
                    )
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.Close, contentDescription = "Close")
                    }
                }

                LinearProgressIndicator(
                    progress = { active.progress },
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(8.dp)),
                    color = accent,
                )

                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                ) {
                    Box(
                        modifier = Modifier
                            .size(220.dp)
                            .clip(CircleShape)
                            .background(accent.copy(alpha = 0.15f)),
                        contentAlignment = Alignment.Center,
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = phaseLabel,
                                style = MaterialTheme.typography.labelLarge,
                                color = accent,
                            )
                            Text(
                                text = formatTime(active.phase.remainingSeconds),
                                fontSize = 56.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onBackground,
                            )
                        }
                    }

                    Text(
                        text = currentName,
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center,
                    )

                    active.currentExercise?.let { exercise ->
                        if (!active.phase.isRestPhase) {
                            Text(
                                text = exercise.instructions,
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.padding(horizontal = 12.dp),
                            )
                        }
                    }

                    active.nextExercise?.let { next ->
                        Text(
                            text = "Up next: ${next.name}",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                    }
                }

                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                    ) {
                        OutlinedButton(
                            onClick = { viewModel.onIntent(ActiveWorkoutIntent.TogglePause) },
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(14.dp),
                        ) {
                            Icon(
                                imageVector = if (active.phase.isPaused) {
                                    Icons.Default.PlayArrow
                                } else {
                                    Icons.Default.Pause
                                },
                                contentDescription = null,
                            )
                            Text(
                                text = if (active.phase.isPaused) "Resume" else "Pause",
                                modifier = Modifier.padding(start = 6.dp),
                            )
                        }
                        OutlinedButton(
                            onClick = { viewModel.onIntent(ActiveWorkoutIntent.SkipPhase) },
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(14.dp),
                        ) {
                            Icon(Icons.Default.SkipNext, contentDescription = null)
                            Text(text = "Skip", modifier = Modifier.padding(start = 6.dp))
                        }
                    }
                    Button(
                        onClick = { viewModel.onIntent(ActiveWorkoutIntent.FinishWorkout) },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(14.dp),
                    ) {
                        Text("Finish Workout", modifier = Modifier.padding(vertical = 6.dp))
                    }
                }
            }
        }
    }
}

private fun formatTime(seconds: Int): String {
    val minutes = seconds / 60
    val remaining = seconds % 60
    return "%d:%02d".format(minutes, remaining)
}
