package com.missclick.habitstracker.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.missclick.habitstracker.database.entity.QuoteEntity

@Dao
interface QuoteDao {
    @Query("SELECT * FROM quote_cache WHERE date = :date LIMIT 1")
    suspend fun getByDate(date: String): QuoteEntity?

    @Upsert
    suspend fun upsert(quote: QuoteEntity)
}