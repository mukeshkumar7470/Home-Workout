package com.example.homeworkout.data.local

import com.example.homeworkout.data.local.dao.UserProfileDao
import com.example.homeworkout.data.local.entity.UserProfileEntity
import com.example.homeworkout.domain.model.FitnessGoal
import com.example.homeworkout.domain.model.FitnessLevel
import com.example.homeworkout.domain.model.UserProfile

class UserProfileLocalDataSource(
    private val userProfileDao: UserProfileDao,
) {

    suspend fun getProfile(): UserProfile {
        val entity = userProfileDao.getProfile()
        return entity?.toDomain() ?: UserProfile()
    }

    suspend fun isOnboardingCompleted(): Boolean =
        userProfileDao.getProfile()?.onboardingCompleted == true

    suspend fun saveProfile(profile: UserProfile) {
        userProfileDao.upsert(profile.toEntity())
    }

    private fun UserProfileEntity.toDomain(): UserProfile = UserProfile(
        age = age,
        weightKg = weightKg,
        fitnessGoal = fitnessGoal?.let { FitnessGoal.fromRaw(it) },
        fitnessLevel = fitnessLevel?.let { FitnessLevel.fromRaw(it) },
        onboardingCompleted = onboardingCompleted,
    )

    private fun UserProfile.toEntity(): UserProfileEntity = UserProfileEntity(
        age = age,
        weightKg = weightKg,
        fitnessGoal = fitnessGoal?.name,
        fitnessLevel = fitnessLevel?.name,
        onboardingCompleted = onboardingCompleted,
    )
}
