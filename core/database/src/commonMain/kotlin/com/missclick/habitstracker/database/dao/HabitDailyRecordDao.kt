package com.missclick.habitstracker.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.missclick.habitstracker.database.entity.HabitDailyRecordEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface HabitDailyRecordDao {
    @Query("SELECT * FROM habit_daily_records WHERE date = :date")
    fun observeForDate(date: String): Flow<List<HabitDailyRecordEntity>>

    @Query("SELECT * FROM habit_daily_records WHERE habitId = :habitId AND date = :date LIMIT 1")
    suspend fun getRecord(habitId: String, date: String): HabitDailyRecordEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(record: HabitDailyRecordEntity)
}