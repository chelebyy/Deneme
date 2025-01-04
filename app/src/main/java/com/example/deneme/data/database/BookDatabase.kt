package com.example.deneme.data.database

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Entity(tableName = "books")
data class BookEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val title: String,
    val author: String,
    val pageCount: Int,
    val status: String,
    val category: String,
    val currentPage: Int = 0,
    val startDate: Long? = null,
    val finishDate: Long? = null
)

@Entity(tableName = "reading_goals")
data class ReadingGoalEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val targetBooks: Int,
    val completedBooks: Int = 0
)

@Dao
interface BookDao {
    @Query("SELECT * FROM books")
    fun getAllBooks(): Flow<List<BookEntity>>
    
    @Query("SELECT * FROM books WHERE title LIKE '%' || :query || '%' OR author LIKE '%' || :query || '%'")
    fun searchBooks(query: String): Flow<List<BookEntity>>
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBook(book: BookEntity)
    
    @Update
    suspend fun updateBook(book: BookEntity)
    
    @Delete
    suspend fun deleteBook(book: BookEntity)
}

@Dao
interface ReadingGoalDao {
    @Query("SELECT * FROM reading_goals")
    fun getAllGoals(): Flow<List<ReadingGoalEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertGoal(goal: ReadingGoalEntity)

    @Update
    suspend fun updateGoal(goal: ReadingGoalEntity)

    @Delete
    suspend fun deleteGoal(goal: ReadingGoalEntity)
}

@Database(entities = [BookEntity::class, ReadingGoalEntity::class], version = 1, exportSchema = false)
abstract class BookDatabase : RoomDatabase() {
    abstract fun bookDao(): BookDao
    abstract fun readingGoalDao(): ReadingGoalDao

    companion object {
        const val DATABASE_NAME = "book_database"
    }
}