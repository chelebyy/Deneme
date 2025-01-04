package com.example.deneme.data.api

import com.example.deneme.data.model.Book
import com.example.deneme.data.model.ReadingStatus
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface GoogleBooksService {
    @GET("volumes")
    suspend fun searchBooks(
        @Query("q") query: String
    ): Response<GoogleBooksResponse>
}

data class GoogleBooksResponse(
    val items: List<VolumeInfo>? = null
)

data class VolumeInfo(
    val volumeInfo: BookInfo
) {
    fun toBook(): Book {
        return Book(
            title = volumeInfo.title ?: "",
            author = volumeInfo.authors?.firstOrNull() ?: "Bilinmeyen Yazar",
            pageCount = volumeInfo.pageCount ?: 0,
            status = ReadingStatus.TO_READ,
            category = volumeInfo.categories?.firstOrNull() ?: "Genel"
        )
    }
}

data class BookInfo(
    val title: String? = null,
    val authors: List<String>? = null,
    val pageCount: Int? = null,
    val categories: List<String>? = null
)