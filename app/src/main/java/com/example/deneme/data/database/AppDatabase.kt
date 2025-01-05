package com.example.deneme.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.deneme.data.dao.BookDao
import com.example.deneme.data.dao.ReadingGoalDao
import com.example.deneme.data.model.Book
import com.example.deneme.data.model.ReadingGoal

@Database(entities = [Book::class, ReadingGoal::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun bookDao(): BookDao
    abstract fun readingGoalDao(): ReadingGoalDao
} 