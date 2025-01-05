package com.example.deneme.data.api

import com.example.deneme.data.model.Book
import retrofit2.http.GET
import retrofit2.http.Query

interface GoogleBooksService {
    @GET("volumes")
    suspend fun searchBooks(@Query("q") query: String): BooksResponse
}

data class BooksResponse(
    val items: List<VolumeInfo>? = null
)

data class VolumeInfo(
    val volumeInfo: BookInfo
)

data class BookInfo(
    val title: String,
    val authors: List<String>? = null,
    val pageCount: Int? = null
)