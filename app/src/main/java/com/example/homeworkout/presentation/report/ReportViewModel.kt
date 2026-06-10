package com.example.homeworkout.presentation.report

import androidx.lifecycle.viewModelScope
import com.example.homeworkout.domain.usecase.GetUserProgressUseCase
import com.example.homeworkout.presentation.mvi.MviViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.launch

@HiltViewModel
class ReportViewModel @Inject constructor(
    private val getUserProgressUseCase: GetUserProgressUseCase,
) : MviViewModel<ReportIntent, ReportState, ReportEffect>(ReportState()) {

    init {
        onIntent(ReportIntent.Load)
    }

    override fun onIntent(intent: ReportIntent) {
        when (intent) {
            ReportIntent.Load, ReportIntent.Refresh -> loadProgress()
        }
    }

    private fun loadProgress() {
        viewModelScope.launch {
            setState { copy(isLoading = true, errorMessage = null) }
            runCatching {
                val progress = getUserProgressUseCase()
                setState { copy(isLoading = false, progress = progress) }
            }.onFailure { error ->
                setState {
                    copy(
                        isLoading = false,
                        errorMessage = error.message ?: "Failed to load progress",
                    )
                }
            }
        }
    }
}
