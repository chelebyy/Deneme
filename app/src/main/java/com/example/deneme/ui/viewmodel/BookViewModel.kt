package com.example.deneme.ui.viewmodel

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.deneme.data.model.Book
import com.example.deneme.data.model.ReadingStatus
import com.example.deneme.data.repository.BookRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
import javax.inject.Inject

interface GoogleBooksApi {
    @GET("volumes")
    suspend fun searchBooks(@Query("q") query: String): BooksResponse
}

data class BooksResponse(
    val items: List<VolumeInfo>? = null
)

data class VolumeInfo(
    val volumeInfo: BookInfo
)

data class BookInfo(
    val title: String,
    val authors: List<String>? = null,
    val pageCount: Int? = null
)

@HiltViewModel
class BookViewModel @Inject constructor(
    private val bookRepository: BookRepository
) : ViewModel() {
    private val _searchQuery = MutableStateFlow("")
    val searchQuery = _searchQuery.asStateFlow()
    
    private val _books = MutableStateFlow<List<Book>>(emptyList())
    val books = _books.asStateFlow()
    
    val searchResults = mutableStateListOf<Book>()

    private val retrofit = Retrofit.Builder()
        .baseUrl("https://www.googleapis.com/books/v1/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val googleBooksApi = retrofit.create(GoogleBooksApi::class.java)

    init {
        loadBooks()
    }

    fun loadBooks() {
        viewModelScope.launch {
            bookRepository.getAllBooks().collect { bookList ->
                _books.value = bookList
            }
        }
    }

    fun searchBooks(query: String) {
        viewModelScope.launch {
            try {
                if (query.length >= 3) {
                    val response = googleBooksApi.searchBooks(query)
                    searchResults.clear()
                    response.items?.forEach { volume ->
                        searchResults.add(
                            Book(
                                title = volume.volumeInfo.title,
                                author = volume.volumeInfo.authors?.firstOrNull() ?: "Bilinmeyen Yazar",
                                pageCount = volume.volumeInfo.pageCount ?: 0,
                                status = ReadingStatus.TO_READ,
                                category = "Genel"
                            )
                        )
                    }
                }
            } catch (e: Exception) {
                // Handle error
            }
        }
        _searchQuery.value = query
    }

    fun addBook(book: Book) {
        viewModelScope.launch {
            bookRepository.insertBook(book)
            loadBooks() // Kitap ekledikten sonra listeyi güncelle
        }
    }

    fun updateBook(book: Book) {
        viewModelScope.launch {
            bookRepository.updateBook(book)
            loadBooks() // Kitap güncelledikten sonra listeyi güncelle
        }
    }

    fun deleteBook(book: Book) {
        viewModelScope.launch {
            bookRepository.deleteBook(book)
            loadBooks() // Kitap sildikten sonra listeyi güncelle
        }
    }
}