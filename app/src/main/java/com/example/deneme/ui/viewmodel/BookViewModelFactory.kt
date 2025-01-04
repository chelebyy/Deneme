package com.example.deneme.ui.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.deneme.data.database.BookDatabase
import com.example.deneme.data.repository.BookRepository
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object BookViewModelFactory {
    fun provideFactory(context: Context) = viewModelFactory {
        initializer {
            val database = BookDatabase.getDatabase(context)
            val retrofit = Retrofit.Builder()
                .baseUrl("https://www.googleapis.com/books/v1/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
            val googleBooksService = retrofit.create(com.example.deneme.data.api.GoogleBooksService::class.java)
            val repository = BookRepository(database.bookDao())
            BookViewModel(repository, googleBooksService)
        }
    }
} 