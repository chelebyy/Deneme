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
    val status: ReadingStatus
)

enum class ReadingStatus {
    TO_READ, READING, READ
}