package com.missclick.habitstracker.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "daily_reflections")
data class DailyReflectionEntity(
    @PrimaryKey val date: String,
    val mood: String?,
    val note: String,
)