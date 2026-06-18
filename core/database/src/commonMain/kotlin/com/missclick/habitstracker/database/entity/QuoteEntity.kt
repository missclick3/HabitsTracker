package com.missclick.habitstracker.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "quote_cache")
data class QuoteEntity(
    @PrimaryKey val date: String,
    val text: String,
    val author: String,
)