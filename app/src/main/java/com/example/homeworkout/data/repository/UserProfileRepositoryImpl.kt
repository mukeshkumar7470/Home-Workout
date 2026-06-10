package com.example.homeworkout.data.repository

import com.example.homeworkout.data.local.UserProfileLocalDataSource
import com.example.homeworkout.domain.model.UserProfile
import com.example.homeworkout.domain.repository.UserProfileRepository

class UserProfileRepositoryImpl(
    private val localDataSource: UserProfileLocalDataSource,
) : UserProfileRepository {

    override suspend fun getProfile(): UserProfile = localDataSource.getProfile()

    override suspend fun isOnboardingCompleted(): Boolean =
        localDataSource.isOnboardingCompleted()

    override suspend fun saveProfile(profile: UserProfile) {
        localDataSource.saveProfile(profile)
    }
}
