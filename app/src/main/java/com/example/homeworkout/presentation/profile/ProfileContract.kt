package com.example.homeworkout.presentation.profile

import com.example.homeworkout.domain.model.UserProfile
import com.example.homeworkout.domain.model.UserProgress
import com.example.homeworkout.presentation.mvi.MviEffect
import com.example.homeworkout.presentation.mvi.MviIntent
import com.example.homeworkout.presentation.mvi.MviState

sealed interface ProfileIntent : MviIntent {
    data object Load : ProfileIntent
}

data class ProfileState(
    val isLoading: Boolean = true,
    val progress: UserProgress = UserProgress(),
    val profile: UserProfile = UserProfile(),
    val errorMessage: String? = null,
) : MviState

sealed interface ProfileEffect : MviEffect
