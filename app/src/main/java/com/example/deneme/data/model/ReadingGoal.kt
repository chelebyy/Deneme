package com.example.deneme.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "reading_goals")
data class ReadingGoal(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val year: Int,
    val month: Int? = null, // null ise yıllık hedef
    val targetBooks: Int,
    val targetPages: Int? = null // opsiyonel sayfa hedefi
) 