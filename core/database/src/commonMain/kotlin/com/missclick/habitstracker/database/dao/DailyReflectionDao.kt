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

    @Query("SELECT * FROM daily_reflections ORDER BY date DESC")
    fun observeAll(): Flow<List<DailyReflectionEntity>>

    @Query("SELECT COUNT(*) FROM daily_reflections")
    suspend fun count(): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(reflection: DailyReflectionEntity)
}