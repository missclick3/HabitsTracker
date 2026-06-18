package com.missclick.habitstracker.database.di

import androidx.room.Room
import androidx.room.RoomDatabase
import com.missclick.habitstracker.database.HabitsDatabase
import java.io.File

internal actual fun createDatabaseBuilder(): RoomDatabase.Builder<HabitsDatabase> {
    val dir = File(System.getProperty("user.home"), ".habits_tracker")
    dir.mkdirs()
    return Room.databaseBuilder<HabitsDatabase>(
        name = File(dir, "habits_tracker.db").absolutePath,
    )
}