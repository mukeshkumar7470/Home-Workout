package com.example.homeworkout.domain.repository

import com.example.homeworkout.domain.model.UserProfile

interface UserProfileRepository {
    suspend fun getProfile(): UserProfile
    suspend fun isOnboardingCompleted(): Boolean
    suspend fun saveProfile(profile: UserProfile)
}
