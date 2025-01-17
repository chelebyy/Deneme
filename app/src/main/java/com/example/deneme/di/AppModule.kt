package com.example.deneme.di

import android.content.Context
import androidx.room.Room
import com.example.deneme.data.api.GoogleBooksApi
import com.example.deneme.data.dao.BookDao
import com.example.deneme.data.dao.ReadingGoalDao
import com.example.deneme.data.repository.BookRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    
    @Provides
    @Singleton
    fun provideBookRepository(
        bookDao: BookDao,
        readingGoalDao: ReadingGoalDao
    ): BookRepository {
        return BookRepository(bookDao, readingGoalDao)
    }
} 