package com.example.homeworkout.domain.usecase

import com.example.homeworkout.domain.model.UserProfile
import com.example.homeworkout.domain.repository.UserProfileRepository

class GetUserProfileUseCase(
    private val repository: UserProfileRepository,
) {
    suspend operator fun invoke(): UserProfile = repository.getProfile()
}

class IsOnboardingCompletedUseCase(
    private val repository: UserProfileRepository,
) {
    suspend operator fun invoke(): Boolean = repository.isOnboardingCompleted()
}

class SaveUserProfileUseCase(
    private val repository: UserProfileRepository,
) {
    suspend operator fun invoke(profile: UserProfile) = repository.saveProfile(profile)
}
