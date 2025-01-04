package com.example.deneme.data.repository

import com.example.deneme.data.database.ReadingGoalDao
import com.example.deneme.data.database.ReadingGoalEntity
import com.example.deneme.data.model.ReadingGoal
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class ReadingGoalRepository @Inject constructor(
    private val readingGoalDao: ReadingGoalDao
) {
    fun getCurrentGoal(): Flow<ReadingGoal?> =
        readingGoalDao.getAllGoals()
            .map { goals ->
                goals.firstOrNull()?.toDomainModel()
            }

    suspend fun updateGoal(goal: ReadingGoal) {
        readingGoalDao.insertGoal(goal.toEntity())
    }

    private fun ReadingGoalEntity.toDomainModel() = ReadingGoal(
        id = id,
        targetBooks = targetBooks,
        completedBooks = completedBooks
    )

    private fun ReadingGoal.toEntity() = ReadingGoalEntity(
        id = id,
        targetBooks = targetBooks,
        completedBooks = completedBooks
    )
}