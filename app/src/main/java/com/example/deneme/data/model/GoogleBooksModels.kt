package com.example.deneme.data.model

data class BooksResponse(
    val items: List<VolumeInfo>? = null,
    val totalItems: Int = 0
)

data class VolumeInfo(
    val id: String,
    val volumeInfo: BookInfo
)

data class BookInfo(
    val title: String,
    val authors: List<String>? = null,
    val pageCount: Int? = null,
    val imageLinks: ImageLinks? = null
)

data class ImageLinks(
    val thumbnail: String? = null
) 