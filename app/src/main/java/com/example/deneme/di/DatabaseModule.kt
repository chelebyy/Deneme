package com.example.deneme.di

import android.content.Context
import androidx.room.Room
import com.example.deneme.data.database.AppDatabase
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
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "app_database"
        ).build()
    }

    @Provides
    @Singleton
    fun provideBookDao(database: AppDatabase): BookDao {
        return database.bookDao()
    }

    @Provides
    @Singleton
    fun provideReadingGoalDao(database: AppDatabase): ReadingGoalDao {
        return database.readingGoalDao()
    }
}