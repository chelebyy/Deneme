package com.example.deneme.di

import android.content.Context
import androidx.room.Room
import com.example.deneme.data.database.BookDatabase
import com.example.deneme.data.dao.BookDao
import com.example.deneme.data.dao.ReadingGoalDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    
    @Provides
    @Singleton
    fun provideBookDatabase(@ApplicationContext context: Context): BookDatabase {
        return Room.databaseBuilder(
            context,
            BookDatabase::class.java,
            BookDatabase.DATABASE_NAME
        )
        .fallbackToDestructiveMigration()
        .build()
    }

    @Provides
    @Singleton
    fun provideBookDao(database: BookDatabase): BookDao {
        return database.bookDao()
    }

    @Provides
    @Singleton
    fun provideReadingGoalDao(database: BookDatabase): ReadingGoalDao {
        return database.readingGoalDao()
    }
}