package com.example.deneme.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "books")
data class Book(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val title: String,
    val author: String,
    val pageCount: Int,
    val status: ReadingStatus,
    val category: String = "",
    val currentPage: Int = 0,
    val startDate: Long? = null,
    val finishDate: Long? = null
)

enum class ReadingStatus {
    TO_READ, READING, READ
}