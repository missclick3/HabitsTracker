package com.missclick.habitstracker.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.missclick.habitstracker.database.entity.DailyReflectionEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface DailyReflectionDao {
    @Query("SELECT * FROM daily_reflections WHERE date = :date LIMIT 1")
    fun observeForDate(date: String): Flow<DailyReflectionEntity?>

    @Query("SELECT * FROM daily_reflections WHERE date = :date LIMIT 1")
    suspend fun getForDate(date: String): DailyReflectionEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(reflection: DailyReflectionEntity)
}