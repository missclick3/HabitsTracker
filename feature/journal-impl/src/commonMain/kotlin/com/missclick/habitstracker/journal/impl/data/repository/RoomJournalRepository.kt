package com.missclick.habitstracker.journal.impl.data.repository

import com.missclick.habitstracker.core.model.Mood
import com.missclick.habitstracker.database.dao.DailyReflectionDao
import com.missclick.habitstracker.database.entity.DailyReflectionEntity
import com.missclick.habitstracker.journal.impl.domain.model.JournalEntry
import com.missclick.habitstracker.journal.impl.domain.repository.IJournalRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlin.time.Clock
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.minus
import kotlinx.datetime.todayIn

internal class RoomJournalRepository(
    private val dao: DailyReflectionDao,
) : IJournalRepository {

    override fun observeJournal(): Flow<List<JournalEntry>> =
        dao.observeAll().map { entities ->
            entities.mapNotNull { it.toEntry() }
                .filter { it.mood != null || it.note.isNotBlank() }
        }

    suspend fun seedIfEmpty() {
        if (dao.count() > 0) return
        val today = Clock.System.todayIn(TimeZone.currentSystemDefault())
        val samples = listOf(
            today.minus(1, DateTimeUnit.DAY) to (Mood.Great to "Had a wonderful day, finished the project on time!"),
            today.minus(2, DateTimeUnit.DAY) to (Mood.Good to "Productive morning, went for a run in the evening."),
            today.minus(3, DateTimeUnit.DAY) to (Mood.Neutral to "Nothing special, just a regular day."),
            today.minus(4, DateTimeUnit.DAY) to (Mood.Low to "Feeling a bit tired today."),
            today.minus(5, DateTimeUnit.DAY) to (Mood.Great to "Amazing workout session!"),
            today.minus(6, DateTimeUnit.DAY) to (Mood.Good to "Good meeting with the team, things are progressing."),
            today.minus(7, DateTimeUnit.DAY) to (Mood.Drained to "Rough day, couldn't focus at all."),
        )
        samples.forEach { (date, moodNote) ->
            dao.upsert(DailyReflectionEntity(date.toString(), moodNote.first.name, moodNote.second))
        }
    }

    private fun DailyReflectionEntity.toEntry(): JournalEntry? {
        val date = runCatching { LocalDate.parse(this.date) }.getOrNull() ?: return null
        val mood = this.mood?.let { runCatching { Mood.valueOf(it) }.getOrNull() }
        return JournalEntry(date = date, mood = mood, note = note)
    }
}