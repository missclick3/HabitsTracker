package com.missclick.habitstracker.database.di

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import com.missclick.habitstracker.database.HabitsDatabase
import org.koin.java.KoinJavaComponent

internal actual fun createDatabaseBuilder(): RoomDatabase.Builder<HabitsDatabase> {
    val ctx = KoinJavaComponent.get<Context>(Context::class.java)
    return Room.databaseBuilder<HabitsDatabase>(
        context = ctx.applicationContext,
        name = ctx.getDatabasePath("habits_tracker.db").absolutePath,
    )
}