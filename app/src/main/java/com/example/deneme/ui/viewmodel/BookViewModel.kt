package com.example.deneme.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.deneme.data.api.GoogleBooksApi
import com.example.deneme.data.model.Book
import com.example.deneme.data.model.ReadingStatus
import com.example.deneme.data.repository.BookRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BookViewModel @Inject constructor(
    private val repository: BookRepository,
    private val googleBooksApi: GoogleBooksApi
) : ViewModel() {

    private val _books = MutableStateFlow<List<Book>>(emptyList())
    val allBooks: StateFlow<List<Book>> = _books.asStateFlow()

    init {
        println("BookViewModel init başladı")
        loadBooks()
    }

    private fun loadBooks() {
        viewModelScope.launch {
            println("Kitaplar yükleniyor...")
            try {
                repository.getAllBooks()
                    .catch { e ->
                        println("Kitapları yüklerken hata: ${e.message}")
                        e.printStackTrace()
                        emit(emptyList())
                    }
                    .collect { books ->
                        println("Kitaplar başarıyla yüklendi. Toplam: ${books.size} kitap")
                        _books.value = books
                    }
            } catch (e: Exception) {
                println("Kitapları yüklerken beklenmeyen hata: ${e.message}")
                e.printStackTrace()
            }
        }
    }

    fun addBook(book: Book) {
        viewModelScope.launch {
            try {
                val newBook = book.copy(id = 0)
                println("Kitap ekleniyor: ${newBook.title}")
                repository.insertBook(newBook)
                println("Kitap başarıyla eklendi: ${newBook.title}")
                // Kitapları yeniden yükle
                loadBooks()
            } catch (e: Exception) {
                println("Kitap eklenirken hata: ${e.message}")
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