package com.example.deneme.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.deneme.data.model.Book
import com.example.deneme.data.model.ReadingGoal
import com.example.deneme.data.model.ReadingStatus
import com.example.deneme.data.repository.BookRepository
import com.example.deneme.data.repository.ReadingGoalRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class ReadingGoalViewModel @Inject constructor(
    private val readingGoalRepository: ReadingGoalRepository,
    private val bookRepository: BookRepository
) : ViewModel() {
    private val currentDate = LocalDate.now()
    
    val currentYearGoal = readingGoalRepository.getYearlyGoal(currentDate.year)
    val currentMonthGoal = readingGoalRepository.getMonthlyGoal(currentDate.year, currentDate.monthValue)
    
    private val _yearProgress = MutableStateFlow<GoalProgress?>(null)
    val yearProgress: StateFlow<GoalProgress?> = _yearProgress.asStateFlow()
    
    private val _monthProgress = MutableStateFlow<GoalProgress?>(null)
    val monthProgress: StateFlow<GoalProgress?> = _monthProgress.asStateFlow()
    
    init {
        viewModelScope.launch {
            // Yıllık ilerlemeyi hesapla
            combine(
                currentYearGoal,
                bookRepository.getAllBooks()
            ) { goal, books ->
                goal?.let {
                    val readBooks = books.count { book ->
                        book.status == ReadingStatus.READ &&
                        LocalDate.now().year == currentDate.year
                    }
                    val readPages = books
                        .filter { book ->
                            book.status == ReadingStatus.READ &&
                            LocalDate.now().year == currentDate.year
                        }
                        .sumOf { it.pageCount }
                    
                    GoalProgress(
                        completedBooks = readBooks,
                        targetBooks = it.targetBooks,
                        completedPages = readPages,
                        targetPages = it.targetPages
                    )
                }
            }.collect { progress ->
                _yearProgress.value = progress
            }
            
            // Aylık ilerlemeyi hesapla
            combine(
                currentMonthGoal,
                bookRepository.getAllBooks()
            ) { goal, books ->
                goal?.let {
                    val readBooks = books.count { book ->
                        book.status == ReadingStatus.READ &&
                        LocalDate.now().year == currentDate.year &&
                        LocalDate.now().monthValue == currentDate.monthValue
                    }
                    val readPages = books
                        .filter { book ->
                            book.status == ReadingStatus.READ &&
                            LocalDate.now().year == currentDate.year &&
                            LocalDate.now().monthValue == currentDate.monthValue
                        }
                        .sumOf { it.pageCount }
                    
                    GoalProgress(
                        completedBooks = readBooks,
                        targetBooks = it.targetBooks,
                        completedPages = readPages,
                        targetPages = it.targetPages
                    )
                }
            }.collect { progress ->
                _monthProgress.value = progress
            }
        }
    }
    
    fun setYearlyGoal(targetBooks: Int, targetPages: Int? = null) {
        viewModelScope.launch {
            readingGoalRepository.insertGoal(
                ReadingGoal(
                    year = currentDate.year,
                    month = null,
                    targetBooks = targetBooks,
                    targetPages = targetPages
                )
            )
        }
    }
    
    fun setMonthlyGoal(targetBooks: Int, targetPages: Int? = null) {
        viewModelScope.launch {
            readingGoalRepository.insertGoal(
                ReadingGoal(
                    year = currentDate.year,
                    month = currentDate.monthValue,
                    targetBooks = targetBooks,
                    targetPages = targetPages
                )
            )
        }
    }
}

data class GoalProgress(
    val completedBooks: Int,
    val targetBooks: Int,
    val completedPages: Int,
    val targetPages: Int?
) {
    val bookProgressPercentage: Float
        get() = (completedBooks.toFloat() / targetBooks) * 100
    
    val pageProgressPercentage: Float?
        get() = targetPages?.let { (completedPages.toFloat() / it) * 100 }
} 