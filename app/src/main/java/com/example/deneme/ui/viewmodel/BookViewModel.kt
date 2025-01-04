package com.example.deneme.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.deneme.data.api.GoogleBooksService
import com.example.deneme.data.model.Book
import com.example.deneme.data.model.ReadingStatus
import com.example.deneme.data.repository.BookRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject
import com.example.deneme.data.api.VolumeInfo

@HiltViewModel
class BookViewModel @Inject constructor(
    private val repository: BookRepository,
    private val googleBooksService: GoogleBooksService
) : ViewModel() {
    val books: Flow<List<Book>> = repository.getAllBooks()
    
    private val _searchResults = MutableStateFlow<List<VolumeInfo>>(emptyList())
    val searchResults: StateFlow<List<VolumeInfo>> = _searchResults.asStateFlow()

    fun addBook(book: Book) {
        viewModelScope.launch {
            repository.insertBook(book)
        }
    }

    fun updateBook(book: Book) {
        viewModelScope.launch {
            repository.updateBook(book)
        }
    }

    fun deleteBook(book: Book) {
        viewModelScope.launch {
            repository.deleteBook(book)
        }
    }

    suspend fun searchBooks(query: String) {
        try {
            val response = googleBooksService.searchBooks(query)
            _searchResults.value = response.items ?: emptyList()
        } catch (e: Exception) {
            _searchResults.value = emptyList()
        }
    }
} 