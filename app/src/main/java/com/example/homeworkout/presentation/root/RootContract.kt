package com.example.homeworkout.presentation.root

import com.example.homeworkout.presentation.mvi.MviEffect
import com.example.homeworkout.presentation.mvi.MviIntent
import com.example.homeworkout.presentation.mvi.MviState

sealed interface RootIntent : MviIntent {
    data object CheckStartup : RootIntent
}

data class RootState(
    val isLoading: Boolean = true,
    val showOnboarding: Boolean = false,
) : MviState

sealed interface RootEffect : MviEffect
