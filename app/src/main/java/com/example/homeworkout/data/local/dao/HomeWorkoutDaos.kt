package com.example.homeworkout.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.homeworkout.data.local.entity.UserProfileEntity
import com.example.homeworkout.data.local.entity.UserStatsEntity
import com.example.homeworkout.data.local.entity.WorkoutHistoryEntity

@Dao
interface WorkoutHistoryDao {

    @Query("SELECT * FROM workout_history ORDER BY completedAtMillis DESC LIMIT :limit")
    suspend fun getRecentHistory(limit: Int = 30): List<WorkoutHistoryEntity>

    @Query("SELECT * FROM workout_history ORDER BY completedAtMillis DESC")
    suspend fun getAllHistory(): List<WorkoutHistoryEntity>

    @Insert
    suspend fun insert(entry: WorkoutHistoryEntity): Long
}

@Dao
interface UserStatsDao {

    @Query("SELECT * FROM user_stats WHERE id = 1")
    suspend fun getStats(): UserStatsEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(stats: UserStatsEntity)
}

@Dao
interface UserProfileDao {

    @Query("SELECT * FROM user_profile WHERE id = 1")
    suspend fun getProfile(): UserProfileEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(profile: UserProfileEntity)
}
