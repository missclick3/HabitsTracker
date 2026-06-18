package com.missclick.habitstracker.database.entity

import androidx.room.Entity

@Entity(
    tableName = "habit_daily_records",
    primaryKeys = ["habitId", "date"],
)
data class HabitDailyRecordEntity(
    val habitId: String,
    val date: String,
    val isCompleted: Boolean,
    val currentCount: Int,
)