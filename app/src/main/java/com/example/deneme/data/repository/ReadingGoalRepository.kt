package com.example.deneme.data.repository

import com.example.deneme.data.dao.ReadingGoalDao
import com.example.deneme.data.model.ReadingGoal
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ReadingGoalRepository @Inject constructor(
    private val readingGoalDao: ReadingGoalDao
) {
    fun getGoalForYear(year: Int): Flow<ReadingGoal?> =
        readingGoalDao.getGoalForYear(year)

    suspend fun setGoal(goal: ReadingGoal) =
        readingGoalDao.insertGoal(goal)

    suspend fun updateGoal(goal: ReadingGoal) =
        readingGoalDao.updateGoal(goal)

    suspend fun deleteGoal(goal: ReadingGoal) =
        readingGoalDao.deleteGoal(goal)

    suspend fun incrementCompletedBooks(year: Int) =
        readingGoalDao.incrementCompletedBooks(year)
}