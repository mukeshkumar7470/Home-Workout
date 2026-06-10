package com.example.homeworkout.presentation.root

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.homeworkout.presentation.navigation.HomeWorkoutNavHost
import com.example.homeworkout.presentation.onboarding.OnboardingScreen

@Composable
fun AppContent(
    modifier: Modifier = Modifier,
    rootViewModel: RootViewModel = hiltViewModel(),
) {
    val state by rootViewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        rootViewModel.onIntent(RootIntent.CheckStartup)
    }

    when {
        state.isLoading -> Box(modifier = modifier.fillMaxSize())

        state.showOnboarding -> OnboardingScreen(
            onFinished = { rootViewModel.onOnboardingFinished() },
        )

        else -> HomeWorkoutNavHost(modifier = modifier)
    }
}
