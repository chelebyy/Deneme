package com.example.deneme.data.model

data class Book(
    val id: Int = 0,
    val title: String,
    val author: String,
    val pageCount: Int,
    val status: ReadingStatus = ReadingStatus.TO_READ,
    val category: String = "Genel",
    val currentPage: Int = 0,
    val startDate: Long? = null,
    val finishDate: Long? = null
)