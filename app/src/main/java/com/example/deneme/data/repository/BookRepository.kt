package com.example.deneme.data.repository

import com.example.deneme.data.dao.BookDao
import com.example.deneme.data.model.Book
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.File
import java.io.FileReader
import java.io.FileWriter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import com.example.deneme.data.dao.ReadingGoalDao
import com.example.deneme.data.model.ReadingGoal

class BookRepository @Inject constructor(
    private val bookDao: BookDao,
    private val readingGoalDao: ReadingGoalDao
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

    data class BackupData(
        val books: List<Book>,
        val readingGoals: List<ReadingGoal>
    )

    suspend fun backupData(file: File) = withContext(Dispatchers.IO) {
        val books = bookDao.getBooksList()
        val goals = readingGoalDao.getAllGoalsList()
        val backupData = BackupData(books, goals)
        
        FileWriter(file).use { writer ->
            Gson().toJson(backupData, writer)
        }
    }

    suspend fun restoreData(file: File) = withContext(Dispatchers.IO) {
        FileReader(file).use { reader ->
            val backupData = Gson().fromJson(reader, BackupData::class.java)
            bookDao.deleteAllBooks()
            readingGoalDao.deleteAllGoals()
            
            backupData.books.forEach { book ->
                bookDao.insertBook(book)
            }
            backupData.readingGoals.forEach { goal ->
                readingGoalDao.insertGoal(goal)
            }
        }
    }
}