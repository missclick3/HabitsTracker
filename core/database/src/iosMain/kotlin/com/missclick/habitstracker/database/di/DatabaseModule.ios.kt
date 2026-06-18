package com.missclick.habitstracker.database.di

import androidx.room.Room
import androidx.room.RoomDatabase
import com.missclick.habitstracker.database.HabitsDatabase
import platform.Foundation.NSDocumentDirectory
import platform.Foundation.NSFileManager
import platform.Foundation.NSUserDomainMask

internal actual fun createDatabaseBuilder(): RoomDatabase.Builder<HabitsDatabase> {
    val documentDir = NSFileManager.defaultManager.URLForDirectory(
        directory = NSDocumentDirectory,
        inDomain = NSUserDomainMask,
        appropriateForURL = null,
        create = false,
        error = null,
    )
    val dbPath = requireNotNull(documentDir?.path) { "Cannot resolve NSDocumentDirectory" }
    return Room.databaseBuilder<HabitsDatabase>(name = "$dbPath/habits_tracker.db")
}