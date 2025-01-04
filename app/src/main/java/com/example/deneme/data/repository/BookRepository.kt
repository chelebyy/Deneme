package com.example.deneme.data.repository

import com.example.deneme.data.dao.BookDao
import com.example.deneme.data.model.Book
import com.example.deneme.data.model.ReadingStatus
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class BookRepository @Inject constructor(
    private val bookDao: BookDao
) {
    fun getAllBooks(): Flow<List<Book>> = bookDao.getAllBooks()
    
    fun getBooksByStatus(status: ReadingStatus): Flow<List<Book>> = bookDao.getBooksByStatus(status)
    
    suspend fun insertBook(book: Book) = bookDao.insertBook(book)
    
    suspend fun updateBook(book: Book) = bookDao.updateBook(book)
    
    suspend fun deleteBook(book: Book) = bookDao.deleteBook(book)
} 