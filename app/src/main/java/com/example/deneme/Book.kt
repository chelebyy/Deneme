package com.example.deneme

data class Book(
    val id: Int,
    val title: String,
    val author: String,
    val pageCount: Int,
    var isRead: Boolean = false
) 