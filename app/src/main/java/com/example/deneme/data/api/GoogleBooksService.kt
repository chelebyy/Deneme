package com.example.deneme.data.api

import com.example.deneme.data.model.Book
import retrofit2.http.GET
import retrofit2.http.Query

interface GoogleBooksService {
    @GET("books/v1/volumes")
    suspend fun searchBooks(
        @Query("q") query: String,
        @Query("maxResults") maxResults: Int = 10
    ): GoogleBooksResponse
}

data class GoogleBooksResponse(
    val items: List<VolumeInfo>? = null
)

data class VolumeInfo(
    val volumeInfo: BookInfo
)

data class BookInfo(
    val title: String = "",
    val authors: List<String>? = null,
    val pageCount: Int = 0
) {
    fun toBook(): Book {
        return Book(
            title = title,
            author = authors?.firstOrNull() ?: "",
            pageCount = pageCount
        )
    }
} 