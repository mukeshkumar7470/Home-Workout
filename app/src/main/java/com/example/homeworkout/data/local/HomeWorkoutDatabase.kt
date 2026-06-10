package com.example.homeworkout.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.homeworkout.data.local.dao.UserProfileDao
import com.example.homeworkout.data.local.dao.UserStatsDao
import com.example.homeworkout.data.local.dao.WorkoutHistoryDao
import com.example.homeworkout.data.local.entity.UserProfileEntity
import com.example.homeworkout.data.local.entity.UserStatsEntity
import com.example.homeworkout.data.local.entity.WorkoutHistoryEntity

@Database(
    entities = [
        WorkoutHistoryEntity::class,
        UserStatsEntity::class,
        UserProfileEntity::class,
    ],
    version = 1,
    exportSchema = false,
)
abstract class HomeWorkoutDatabase : RoomDatabase() {
    abstract fun workoutHistoryDao(): WorkoutHistoryDao
    abstract fun userStatsDao(): UserStatsDao
    abstract fun userProfileDao(): UserProfileDao
}
