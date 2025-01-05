package com.example.deneme.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.deneme.data.model.ReadingGoal
import com.example.deneme.data.repository.ReadingGoalRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import java.util.Calendar
import javax.inject.Inject

@HiltViewModel
class ReadingGoalViewModel @Inject constructor(
    private val repository: ReadingGoalRepository
) : ViewModel() {

    private val currentYear = Calendar.getInstance().get(Calendar.YEAR)
    val currentGoal: Flow<ReadingGoal?> = repository.getGoalForYear(currentYear)

    fun setGoal(targetBooks: Int) {
        viewModelScope.launch {
            repository.setGoal(
                ReadingGoal(
                    year = currentYear,
                    targetBooks = targetBooks,
                    completedBooks = 0
                )
            )
        }
    }

    fun updateGoal(goal: ReadingGoal) {
        viewModelScope.launch {
            repository.updateGoal(goal)
        }
    }

    fun deleteGoal(goal: ReadingGoal) {
        viewModelScope.launch {
            repository.deleteGoal(goal)
        }
    }

    fun incrementCompletedBooks() {
        viewModelScope.launch {
            repository.incrementCompletedBooks(currentYear)
        }
    }
}