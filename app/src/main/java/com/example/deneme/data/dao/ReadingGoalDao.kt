package com.example.deneme.data.dao

import androidx.room.*
import com.example.deneme.data.model.ReadingGoal
import kotlinx.coroutines.flow.Flow

@Dao
interface ReadingGoalDao {
    @Query("SELECT * FROM reading_goals")
    fun getAllGoals(): Flow<List<ReadingGoal>>

    @Query("SELECT * FROM reading_goals WHERE year = :year")
    fun getGoalForYear(year: Int): Flow<ReadingGoal?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertGoal(goal: ReadingGoal)

    @Update
    suspend fun updateGoal(goal: ReadingGoal)

    @Delete
    suspend fun deleteGoal(goal: ReadingGoal)

    @Query("UPDATE reading_goals SET completedBooks = completedBooks + 1 WHERE year = :year")
    suspend fun incrementCompletedBooks(year: Int)

    @Query("SELECT * FROM reading_goals")
    suspend fun getAllGoalsList(): List<ReadingGoal>

    @Query("DELETE FROM reading_goals")
    suspend fun deleteAllGoals()
} 