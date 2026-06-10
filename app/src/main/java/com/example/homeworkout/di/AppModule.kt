package com.example.homeworkout.di

import android.content.Context
import androidx.room.Room
import com.example.homeworkout.data.local.HomeWorkoutDatabase
import com.example.homeworkout.data.local.RoomProgressDataSource
import com.example.homeworkout.data.local.UserProfileLocalDataSource
import com.example.homeworkout.data.local.WorkoutAssetDataSource
import com.example.homeworkout.data.local.dao.UserProfileDao
import com.example.homeworkout.data.local.dao.UserStatsDao
import com.example.homeworkout.data.local.dao.WorkoutHistoryDao
import com.example.homeworkout.data.local.SettingsDataStore
import com.example.homeworkout.data.repository.SettingsRepositoryImpl
import com.example.homeworkout.data.repository.UserProfileRepositoryImpl
import com.example.homeworkout.data.repository.WorkoutRepositoryImpl
import com.example.homeworkout.domain.repository.SettingsRepository
import com.example.homeworkout.domain.repository.UserProfileRepository
import com.example.homeworkout.domain.repository.WorkoutRepository
import com.example.homeworkout.domain.usecase.GetFeaturedWorkoutUseCase
import com.example.homeworkout.domain.usecase.GetUserProfileUseCase
import com.example.homeworkout.domain.usecase.GetUserProgressUseCase
import com.example.homeworkout.domain.usecase.GetWorkoutByIdUseCase
import com.example.homeworkout.domain.usecase.GetWorkoutsByCategoryUseCase
import com.example.homeworkout.domain.usecase.GetWorkoutsUseCase
import com.example.homeworkout.domain.usecase.IsOnboardingCompletedUseCase
import com.example.homeworkout.domain.usecase.ObserveAppSettingsUseCase
import com.example.homeworkout.domain.usecase.SaveUserProfileUseCase
import com.example.homeworkout.domain.usecase.SaveWorkoutCompletionUseCase
import com.example.homeworkout.domain.usecase.UpdateAppLanguageUseCase
import com.example.homeworkout.domain.usecase.UpdateAppThemeUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): HomeWorkoutDatabase =
        Room.databaseBuilder(
            context,
            HomeWorkoutDatabase::class.java,
            "home_workout.db",
        ).build()

    @Provides
    fun provideWorkoutHistoryDao(database: HomeWorkoutDatabase): WorkoutHistoryDao =
        database.workoutHistoryDao()

    @Provides
    fun provideUserStatsDao(database: HomeWorkoutDatabase): UserStatsDao =
        database.userStatsDao()

    @Provides
    fun provideUserProfileDao(database: HomeWorkoutDatabase): UserProfileDao =
        database.userProfileDao()
}

@Module
@InstallIn(SingletonComponent::class)
object DataModule {

    @Provides
    @Singleton
    fun provideWorkoutAssetDataSource(@ApplicationContext context: Context): WorkoutAssetDataSource =
        WorkoutAssetDataSource(context)

    @Provides
    @Singleton
    fun provideRoomProgressDataSource(
        historyDao: WorkoutHistoryDao,
        statsDao: UserStatsDao,
    ): RoomProgressDataSource = RoomProgressDataSource(historyDao, statsDao)

    @Provides
    @Singleton
    fun provideUserProfileLocalDataSource(
        userProfileDao: UserProfileDao,
    ): UserProfileLocalDataSource = UserProfileLocalDataSource(userProfileDao)

    @Provides
    @Singleton
    fun provideWorkoutRepository(
        assetDataSource: WorkoutAssetDataSource,
        progressDataSource: RoomProgressDataSource,
    ): WorkoutRepository = WorkoutRepositoryImpl(assetDataSource, progressDataSource)

    @Provides
    @Singleton
    fun provideUserProfileRepository(
        localDataSource: UserProfileLocalDataSource,
    ): UserProfileRepository = UserProfileRepositoryImpl(localDataSource)

    @Provides
    @Singleton
    fun provideSettingsDataStore(@ApplicationContext context: Context): SettingsDataStore =
        SettingsDataStore(context)

    @Provides
    @Singleton
    fun provideSettingsRepository(
        settingsDataStore: SettingsDataStore,
    ): SettingsRepository = SettingsRepositoryImpl(settingsDataStore)
}

@Module
@InstallIn(SingletonComponent::class)
object UseCaseModule {

    @Provides
    fun provideGetWorkoutsUseCase(repository: WorkoutRepository): GetWorkoutsUseCase =
        GetWorkoutsUseCase(repository)

    @Provides
    fun provideGetWorkoutByIdUseCase(repository: WorkoutRepository): GetWorkoutByIdUseCase =
        GetWorkoutByIdUseCase(repository)

    @Provides
    fun provideGetFeaturedWorkoutUseCase(repository: WorkoutRepository): GetFeaturedWorkoutUseCase =
        GetFeaturedWorkoutUseCase(repository)

    @Provides
    fun provideGetWorkoutsByCategoryUseCase(repository: WorkoutRepository): GetWorkoutsByCategoryUseCase =
        GetWorkoutsByCategoryUseCase(repository)

    @Provides
    fun provideGetUserProgressUseCase(repository: WorkoutRepository): GetUserProgressUseCase =
        GetUserProgressUseCase(repository)

    @Provides
    fun provideSaveWorkoutCompletionUseCase(repository: WorkoutRepository): SaveWorkoutCompletionUseCase =
        SaveWorkoutCompletionUseCase(repository)

    @Provides
    fun provideGetUserProfileUseCase(repository: UserProfileRepository): GetUserProfileUseCase =
        GetUserProfileUseCase(repository)

    @Provides
    fun provideIsOnboardingCompletedUseCase(repository: UserProfileRepository): IsOnboardingCompletedUseCase =
        IsOnboardingCompletedUseCase(repository)

    @Provides
    fun provideSaveUserProfileUseCase(repository: UserProfileRepository): SaveUserProfileUseCase =
        SaveUserProfileUseCase(repository)

    @Provides
    fun provideObserveAppSettingsUseCase(repository: SettingsRepository): ObserveAppSettingsUseCase =
        ObserveAppSettingsUseCase(repository)

    @Provides
    fun provideUpdateAppLanguageUseCase(repository: SettingsRepository): UpdateAppLanguageUseCase =
        UpdateAppLanguageUseCase(repository)

    @Provides
    fun provideUpdateAppThemeUseCase(repository: SettingsRepository): UpdateAppThemeUseCase =
        UpdateAppThemeUseCase(repository)
}
