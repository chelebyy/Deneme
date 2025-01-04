package com.example.deneme.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.deneme.data.model.ReadingGoal
import com.example.deneme.data.repository.ReadingGoalRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ReadingGoalViewModel @Inject constructor(
    private val repository: ReadingGoalRepository
) : ViewModel() {
    val currentGoal: StateFlow<ReadingGoal?> = repository.getCurrentGoal()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = null
        )

    fun updateYearlyGoal(targetBooks: Int) {
        viewModelScope.launch {
            val currentGoalValue = currentGoal.value
            val updatedGoal = currentGoalValue?.copy(
                targetBooks = targetBooks
            ) ?: ReadingGoal(
                targetBooks = targetBooks,
                completedBooks = 0
            )
            repository.updateGoal(updatedGoal)
        }
    }

    fun updateMonthlyGoal(targetBooks: Int) {
        viewModelScope.launch {
            val currentGoalValue = currentGoal.value
            val updatedGoal = currentGoalValue?.copy(
                targetBooks = targetBooks
            ) ?: ReadingGoal(
                targetBooks = targetBooks,
                completedBooks = 0
            )
            repository.updateGoal(updatedGoal)
        }
    }
}