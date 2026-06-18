package com.missclick.habitstracker.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.missclick.habitstracker.database.dao.DailyReflectionDao
import com.missclick.habitstracker.database.dao.HabitDao
import com.missclick.habitstracker.database.dao.HabitDailyRecordDao
import com.missclick.habitstracker.database.dao.QuoteDao
import com.missclick.habitstracker.database.dao.UserDao
import com.missclick.habitstracker.database.entity.DailyReflectionEntity
import com.missclick.habitstracker.database.entity.HabitDailyRecordEntity
import com.missclick.habitstracker.database.entity.HabitEntity
import com.missclick.habitstracker.database.entity.QuoteEntity
import com.missclick.habitstracker.database.entity.UserEntity

@Database(
    entities = [
        HabitEntity::class,
        HabitDailyRecordEntity::class,
        DailyReflectionEntity::class,
        UserEntity::class,
        QuoteEntity::class,
    ],
    version = 2,
    exportSchema = true,
)
abstract class HabitsDatabase : RoomDatabase() {
    abstract fun habitDao(): HabitDao
    abstract fun habitDailyRecordDao(): HabitDailyRecordDao
    abstract fun dailyReflectionDao(): DailyReflectionDao
    abstract fun userDao(): UserDao
    abstract fun quoteDao(): QuoteDao
}