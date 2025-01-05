package com.example.deneme.data.api

import com.example.deneme.data.model.BooksResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface GoogleBooksApi {
    @GET("books/v1/volumes")
    suspend fun searchBooks(
        @Query("q") query: String,
        @Query("maxResults") maxResults: Int = 20,
        @Query("langRestrict") langRestrict: String = "tr"
    ): BooksResponse
} 