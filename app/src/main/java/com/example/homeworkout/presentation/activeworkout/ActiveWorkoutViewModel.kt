package com.example.homeworkout.presentation.activeworkout

import androidx.lifecycle.viewModelScope
import com.example.homeworkout.domain.model.ActiveWorkoutPhase
import com.example.homeworkout.domain.model.ActiveWorkoutState
import com.example.homeworkout.domain.usecase.GetWorkoutByIdUseCase
import com.example.homeworkout.domain.usecase.SaveWorkoutCompletionUseCase
import com.example.homeworkout.presentation.mvi.MviViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.launch

@HiltViewModel
class ActiveWorkoutViewModel @Inject constructor(
    private val getWorkoutByIdUseCase: GetWorkoutByIdUseCase,
    private val saveWorkoutCompletionUseCase: SaveWorkoutCompletionUseCase,
) : MviViewModel<ActiveWorkoutIntent, ActiveWorkoutUiState, ActiveWorkoutEffect>(
    ActiveWorkoutUiState(),
) {

    override fun onIntent(intent: ActiveWorkoutIntent) {
        when (intent) {
            is ActiveWorkoutIntent.Load -> loadWorkout(intent.workoutId)
            ActiveWorkoutIntent.Tick -> handleTick()
            ActiveWorkoutIntent.TogglePause -> togglePause()
            ActiveWorkoutIntent.SkipPhase -> advancePhase(force = true)
            ActiveWorkoutIntent.FinishWorkout -> completeWorkout()
        }
    }

    private fun loadWorkout(workoutId: String) {
        viewModelScope.launch {
            setState { copy(isLoading = true, errorMessage = null) }
            runCatching {
                val workout = getWorkoutByIdUseCase(workoutId)
                if (workout == null || workout.exercises.isEmpty()) {
                    setState {
                        copy(
                            isLoading = false,
                            errorMessage = "Workout not found",
                        )
                    }
                    return@launch
                }
                val firstExercise = workout.exercises.first()
                setState {
                    copy(
                        isLoading = false,
                        activeWorkout = ActiveWorkoutState(
                            workout = workout,
                            phase = ActiveWorkoutPhase(
                                exerciseIndex = 0,
                                remainingSeconds = firstExercise.durationSeconds,
                                isRestPhase = false,
                                isPaused = false,
                            ),
                            completedExercises = 0,
                        ),
                    )
                }
            }.onFailure { error ->
                setState {
                    copy(
                        isLoading = false,
                        errorMessage = error.message ?: "Failed to start workout",
                    )
                }
            }
        }
    }

    private fun handleTick() {
        if (state.value.isCompleted) return
        val current = state.value.activeWorkout ?: return
        if (current.phase.isPaused || current.isFinished) return

        if (current.phase.remainingSeconds > 1) {
            setState {
                copy(
                    activeWorkout = current.copy(
                        phase = current.phase.copy(
                            remainingSeconds = current.phase.remainingSeconds - 1,
                        ),
                    ),
                )
            }
            return
        }

        advancePhase(force = false)
    }

    private fun togglePause() {
        val current = state.value.activeWorkout ?: return
        setState {
            copy(
                activeWorkout = current.copy(
                    phase = current.phase.copy(isPaused = !current.phase.isPaused),
                ),
            )
        }
    }

    private fun advancePhase(force: Boolean) {
        val current = state.value.activeWorkout ?: return
        val exercise = current.currentExercise ?: return

        if (current.phase.isRestPhase) {
            val nextIndex = current.phase.exerciseIndex + 1
            if (nextIndex >= current.workout.exercises.size) {
                completeWorkout()
                return
            }
            val nextExercise = current.workout.exercises[nextIndex]
            setState {
                copy(
                    activeWorkout = current.copy(
                        phase = ActiveWorkoutPhase(
                            exerciseIndex = nextIndex,
                            remainingSeconds = nextExercise.durationSeconds,
                            isRestPhase = false,
                            isPaused = false,
                        ),
                        completedExercises = current.completedExercises + 1,
                    ),
                )
            }
            return
        }

        if (exercise.restSeconds <= 0 || force) {
            val nextIndex = current.phase.exerciseIndex + 1
            if (nextIndex >= current.workout.exercises.size) {
                completeWorkout()
                return
            }
            val nextExercise = current.workout.exercises[nextIndex]
            setState {
                copy(
                    activeWorkout = current.copy(
                        phase = ActiveWorkoutPhase(
                            exerciseIndex = nextIndex,
                            remainingSeconds = nextExercise.durationSeconds,
                            isRestPhase = false,
                            isPaused = false,
                        ),
                        completedExercises = current.completedExercises + 1,
                    ),
                )
            }
        } else {
            setState {
                copy(
                    activeWorkout = current.copy(
                        phase = ActiveWorkoutPhase(
                            exerciseIndex = current.phase.exerciseIndex,
                            remainingSeconds = exercise.restSeconds,
                            isRestPhase = true,
                            isPaused = false,
                        ),
                    ),
                )
            }
        }
    }

    private fun completeWorkout() {
        val workout = state.value.activeWorkout?.workout ?: return
        if (state.value.isCompleted) return
        viewModelScope.launch {
            saveWorkoutCompletionUseCase(workout)
            setState { copy(isCompleted = true) }
            sendEffect {
                ActiveWorkoutEffect.ShowCompletion(
                    workoutName = workout.name,
                    calories = workout.calories,
                )
            }
        }
    }
}
