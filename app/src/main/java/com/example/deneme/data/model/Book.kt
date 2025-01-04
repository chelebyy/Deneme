package com.example.deneme.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "books")
data class Book(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val title: String,
    val author: String,
    val pageCount: Int = 0,
    val status: ReadingStatus = ReadingStatus.TO_READ,
    val notes: String? = null
)

enum class ReadingStatus {
    TO_READ,
    READING,
    READ
} 