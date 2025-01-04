package com.example.deneme.data.model

enum class ReadingStatus {
    TO_READ,
    READING,
    READ;

    override fun toString(): String {
        return when (this) {
            TO_READ -> "Okunacak"
            READING -> "Okunuyor"
            READ -> "Okundu"
        }
    }
}
