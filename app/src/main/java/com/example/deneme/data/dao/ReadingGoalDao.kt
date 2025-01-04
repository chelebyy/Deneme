package com.example.deneme.data.dao

import androidx.room.*
import com.example.deneme.data.model.ReadingGoal
import kotlinx.coroutines.flow.Flow

@Dao
interface ReadingGoalDao {
    @Query("SELECT * FROM reading_goals WHERE year = :year AND (month = :month OR month IS NULL)")
    fun getGoalsForPeriod(year: Int, month: Int?): Flow<List<ReadingGoal>>

    @Query("SELECT * FROM reading_goals WHERE year = :year AND month IS NULL")
    fun getYearlyGoal(year: Int): Flow<ReadingGoal?>

    @Query("SELECT * FROM reading_goals WHERE year = :year AND month = :month")
    fun getMonthlyGoal(year: Int, month: Int): Flow<ReadingGoal?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertGoal(goal: ReadingGoal)

    @Update
    suspend fun updateGoal(goal: ReadingGoal)

    @Delete
    suspend fun deleteGoal(goal: ReadingGoal)
} 