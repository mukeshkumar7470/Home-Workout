package com.example.homeworkout.presentation.onboarding

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.homeworkout.domain.model.FitnessGoal
import com.example.homeworkout.domain.model.FitnessLevel

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun OnboardingScreen(
    onFinished: () -> Unit,
    viewModel: OnboardingViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(viewModel) {
        viewModel.effect.collect { effect ->
            when (effect) {
                OnboardingEffect.NavigateToMain -> onFinished()
            }
        }
    }

    val stepIndex = when (state.step) {
        OnboardingStep.WELCOME -> 0
        OnboardingStep.AGE -> 1
        OnboardingStep.WEIGHT -> 2
        OnboardingStep.GOAL -> 3
        OnboardingStep.LEVEL -> 4
    }
    val progress = (stepIndex + 1) / 5f

    Scaffold { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 24.dp, vertical = 16.dp),
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                if (state.step != OnboardingStep.WELCOME) {
                    IconButton(onClick = { viewModel.onIntent(OnboardingIntent.Back) }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                } else {
                    Spacer(modifier = Modifier.size(48.dp))
                }
                TextButton(onClick = { viewModel.onIntent(OnboardingIntent.Skip) }) {
                    Text("Skip")
                }
            }

            LinearProgressIndicator(
                progress = { progress },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 12.dp),
            )

            Column(
                modifier = Modifier
                    .weight(1f)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                when (state.step) {
                    OnboardingStep.WELCOME -> WelcomeStep()
                    OnboardingStep.AGE -> AgeStep(
                        value = state.ageInput,
                        onValueChange = { viewModel.onIntent(OnboardingIntent.AgeChanged(it)) },
                    )
                    OnboardingStep.WEIGHT -> WeightStep(
                        value = state.weightInput,
                        onValueChange = { viewModel.onIntent(OnboardingIntent.WeightChanged(it)) },
                    )
                    OnboardingStep.GOAL -> GoalStep(
                        selected = state.selectedGoal,
                        onSelect = { viewModel.onIntent(OnboardingIntent.GoalSelected(it)) },
                    )
                    OnboardingStep.LEVEL -> LevelStep(
                        selected = state.selectedLevel,
                        onSelect = { viewModel.onIntent(OnboardingIntent.LevelSelected(it)) },
                    )
                }

                state.errorMessage?.let { error ->
                    Text(
                        text = error,
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodyMedium,
                    )
                }
            }

            Button(
                onClick = { viewModel.onIntent(OnboardingIntent.Next) },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
            ) {
                Text(
                    text = if (state.step == OnboardingStep.LEVEL) "Get Started" else "Continue",
                    modifier = Modifier.padding(vertical = 8.dp),
                )
            }
        }
    }
}

@Composable
private fun WelcomeStep() {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text(
            text = "Welcome to Home Workout",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
        )
        Text(
            text = "Build muscle, burn fat, and get fit at home with no equipment. Let's personalize your experience.",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        Text(
            text = "We'll ask a few quick questions about your age, weight, and goals to recommend the right workouts.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
    }
}

@Composable
private fun AgeStep(value: String, onValueChange: (String) -> Unit) {
    StepHeader(
        title = "How old are you?",
        subtitle = "This helps us tailor workout intensity for you.",
    )
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = Modifier.fillMaxWidth(),
        label = { Text("Age") },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        singleLine = true,
        shape = RoundedCornerShape(14.dp),
    )
}

@Composable
private fun WeightStep(value: String, onValueChange: (String) -> Unit) {
    StepHeader(
        title = "What's your weight?",
        subtitle = "Used to estimate calories burned during workouts.",
    )
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = Modifier.fillMaxWidth(),
        label = { Text("Weight (kg)") },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
        singleLine = true,
        shape = RoundedCornerShape(14.dp),
    )
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun GoalStep(
    selected: FitnessGoal?,
    onSelect: (FitnessGoal) -> Unit,
) {
    StepHeader(
        title = "What's your main goal?",
        subtitle = "Choose the outcome that matters most to you.",
    )
    FlowRow(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        FitnessGoal.entries.forEach { goal ->
            FilterChip(
                selected = selected == goal,
                onClick = { onSelect(goal) },
                label = { Text(goal.displayName) },
            )
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun LevelStep(
    selected: FitnessLevel?,
    onSelect: (FitnessLevel) -> Unit,
) {
    StepHeader(
        title = "What's your fitness level?",
        subtitle = "We'll match workouts to your experience.",
    )
    FlowRow(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        FitnessLevel.entries.forEach { level ->
            FilterChip(
                selected = selected == level,
                onClick = { onSelect(level) },
                label = { Text(level.displayName) },
            )
        }
    }
}

@Composable
private fun StepHeader(title: String, subtitle: String) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text(
            text = title,
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
        )
        Text(
            text = subtitle,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Start,
        )
    }
}
