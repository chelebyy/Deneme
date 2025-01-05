package com.example.deneme.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.deneme.data.api.GoogleBooksApi
import com.example.deneme.data.model.Book
import com.example.deneme.data.model.ReadingStatus
import com.example.deneme.data.repository.BookRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BookViewModel @Inject constructor(
    private val repository: BookRepository,
    private val googleBooksApi: GoogleBooksApi
) : ViewModel() {

    val allBooks: Flow<List<Book>> = repository.getAllBooks()
        .catch { e ->
            e.printStackTrace()
            emit(emptyList())
        }

    fun addBook(book: Book) {
        viewModelScope.launch {
            try {
                repository.insertBook(book)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun updateBook(book: Book) {
        viewModelScope.launch {
            try {
                repository.updateBook(book)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun deleteBook(book: Book) {
        viewModelScope.launch {
            try {
                repository.deleteBook(book)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun searchBooks(query: String, onResult: (List<Book>) -> Unit) {
        viewModelScope.launch {
            try {
                val response = googleBooksApi.searchBooks(query)
                val books = response.items?.mapNotNull { volume ->
                    try {
                        val bookInfo = volume.volumeInfo
                        if (!bookInfo.title.isNullOrBlank()) {
                            Book(
                                id = 0,
                                title = bookInfo.title,
                                author = bookInfo.authors?.firstOrNull() ?: "Bilinmeyen Yazar",
                                pageCount = bookInfo.pageCount ?: 0,
                                status = ReadingStatus.TO_READ,
                                category = "",
                                currentPage = 0
                            )
                        } else null
                    } catch (e: Exception) {
                        e.printStackTrace()
                        null
                    }
                } ?: emptyList()
                onResult(books)
            } catch (e: Exception) {
                e.printStackTrace()
                onResult(emptyList())
            }
        }
    }
}