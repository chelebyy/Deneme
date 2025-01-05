package com.example.deneme.data.repository

import com.example.deneme.data.dao.BookDao
import com.example.deneme.data.model.Book
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.File

class BookRepository @Inject constructor(
    private val bookDao: BookDao
) {
    fun getAllBooks(): Flow<List<Book>> = bookDao.getAllBooks()

    suspend fun insertBook(book: Book) = bookDao.insertBook(book)

    suspend fun updateBook(book: Book) = bookDao.updateBook(book)

    suspend fun deleteBook(book: Book) = bookDao.deleteBook(book)

    suspend fun backupBooks(file: File) {
        val books = bookDao.getBooksList()
        val gson = Gson()
        val json = gson.toJson(books)
        file.writeText(json)
    }

    suspend fun restoreBooks(file: File) {
        val gson = Gson()
        val json = file.readText()
        val type = object : TypeToken<List<Book>>() {}.type
        val books: List<Book> = gson.fromJson(json, type)
        bookDao.deleteAllBooks()
        books.forEach { bookDao.insertBook(it) }
    }
}