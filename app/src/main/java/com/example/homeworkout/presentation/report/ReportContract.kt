package com.example.homeworkout.presentation.report

import com.example.homeworkout.domain.model.UserProgress
import com.example.homeworkout.presentation.mvi.MviEffect
import com.example.homeworkout.presentation.mvi.MviIntent
import com.example.homeworkout.presentation.mvi.MviState

sealed interface ReportIntent : MviIntent {
    data object Load : ReportIntent
    data object Refresh : ReportIntent
}

data class ReportState(
    val isLoading: Boolean = true,
    val progress: UserProgress = UserProgress(),
    val errorMessage: String? = null,
) : MviState

sealed interface ReportEffect : MviEffect
