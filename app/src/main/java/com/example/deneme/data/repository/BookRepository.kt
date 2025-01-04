package com.example.deneme.data.repository

import com.example.deneme.data.database.BookDao
import com.example.deneme.data.database.BookEntity
import com.example.deneme.data.model.Book
import com.example.deneme.data.model.ReadingStatus
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class BookRepository @Inject constructor(
    private val bookDao: BookDao
) {
    fun getAllBooks(): Flow<List<Book>> = 
        bookDao.getAllBooks().map { entities -> entities.map { it.toDomainModel() } }
    
    fun searchBooks(query: String): Flow<List<Book>> =
        bookDao.searchBooks(query).map { entities -> entities.map { it.toDomainModel() } }

    suspend fun insertBook(book: Book) {
        bookDao.insertBook(book.toEntity())
    }

    suspend fun updateBook(book: Book) {
        bookDao.updateBook(book.toEntity())
    }

    suspend fun deleteBook(book: Book) {
        bookDao.deleteBook(book.toEntity())
    }

    private fun BookEntity.toDomainModel() = Book(
        id = id,
        title = title,
        author = author,
        pageCount = pageCount,
        status = ReadingStatus.valueOf(status),
        category = category,
        currentPage = currentPage,
        startDate = startDate,
        finishDate = finishDate
    )

    private fun Book.toEntity() = BookEntity(
        id = id,
        title = title,
        author = author,
        pageCount = pageCount,
        status = status.name,
        category = category,
        currentPage = currentPage,
        startDate = startDate,
        finishDate = finishDate
    )
}