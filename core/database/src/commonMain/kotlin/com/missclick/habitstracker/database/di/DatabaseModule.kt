package com.missclick.habitstracker.database.di

import androidx.room.RoomDatabase
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import com.missclick.habitstracker.database.HabitsDatabase
import kotlinx.coroutines.Dispatchers
import org.koin.dsl.module

internal expect fun createDatabaseBuilder(): RoomDatabase.Builder<HabitsDatabase>

val databaseModule = module {
    single<HabitsDatabase> {
        createDatabaseBuilder()
            .setDriver(BundledSQLiteDriver())
            .setQueryCoroutineContext(Dispatchers.IO)
            .build()
    }
    single { get<HabitsDatabase>().habitDao() }
    single { get<HabitsDatabase>().habitDailyRecordDao() }
    single { get<HabitsDatabase>().dailyReflectionDao() }
    single { get<HabitsDatabase>().userDao() }
}