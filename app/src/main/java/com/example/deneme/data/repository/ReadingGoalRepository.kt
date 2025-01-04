package com.example.deneme.data.repository

import com.example.deneme.data.dao.ReadingGoalDao
import com.example.deneme.data.model.ReadingGoal
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ReadingGoalRepository @Inject constructor(
    private val readingGoalDao: ReadingGoalDao
) {
    fun getGoalsForPeriod(year: Int, month: Int?) = readingGoalDao.getGoalsForPeriod(year, month)
    
    fun getYearlyGoal(year: Int) = readingGoalDao.getYearlyGoal(year)
    
    fun getMonthlyGoal(year: Int, month: Int) = readingGoalDao.getMonthlyGoal(year, month)
    
    suspend fun insertGoal(goal: ReadingGoal) = readingGoalDao.insertGoal(goal)
    
    suspend fun updateGoal(goal: ReadingGoal) = readingGoalDao.updateGoal(goal)
    
    suspend fun deleteGoal(goal: ReadingGoal) = readingGoalDao.deleteGoal(goal)
} 