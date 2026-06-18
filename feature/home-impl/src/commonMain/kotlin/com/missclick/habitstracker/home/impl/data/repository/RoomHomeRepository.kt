package com.missclick.habitstracker.home.impl.data.repository

import com.missclick.habitstracker.core.model.HabitId
import com.missclick.habitstracker.core.model.Mood
import com.missclick.habitstracker.database.dao.DailyReflectionDao
import com.missclick.habitstracker.database.dao.HabitDao
import com.missclick.habitstracker.database.dao.HabitDailyRecordDao
import com.missclick.habitstracker.database.entity.DailyReflectionEntity
import com.missclick.habitstracker.database.entity.HabitDailyRecordEntity
import com.missclick.habitstracker.home.impl.data.mapper.defaultHabits
import com.missclick.habitstracker.home.impl.data.mapper.toDomain
import com.missclick.habitstracker.home.impl.data.mapper.toEntity
import com.missclick.habitstracker.home.impl.data.mapper.toHomeHabit
import com.missclick.habitstracker.home.impl.domain.model.Habit
import com.missclick.habitstracker.home.impl.domain.repository.HomeReflection
import com.missclick.habitstracker.home.impl.domain.repository.HomeSnapshot
import com.missclick.habitstracker.home.impl.domain.repository.IHomeRepository
import com.missclick.habitstracker.home.impl.domain.repository.IUserRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine

internal class RoomHomeRepository(
    private val habitDao: HabitDao,
    private val recordDao: HabitDailyRecordDao,
    private val reflectionDao: DailyReflectionDao,
    private val userRepository: IUserRepository,
    private val todayDate: () -> String,
) : IHomeRepository {

    override fun observeHome(): Flow<HomeSnapshot> {
        val today = todayDate()
        return combine(
            habitDao.observeAll(),
            recordDao.observeForDate(today),
            reflectionDao.observeForDate(today),
            userRepository.observeUser(),
        ) { habits, records, reflection, user ->
            val recordMap = records.associateBy { it.habitId }
            HomeSnapshot(
                dateLabel = "",
                userName = user?.name ?: "You",
                habits = habits.map { it.toHomeHabit(recordMap[it.id]) },
                reflection = HomeReflection(
                    selectedMood = reflection?.mood?.let { runCatching { Mood.valueOf(it) }.getOrNull() },
                    note = reflection?.note ?: "",
                ),
            )
        }
    }

    override suspend fun toggleHabit(habitId: HabitId) {
        val today = todayDate()
        val current = recordDao.getRecord(habitId.value, today)
        val next = (current ?: HabitDailyRecordEntity(habitId.value, today, false, 0))
            .copy(isCompleted = !(current?.isCompleted ?: false))
        recordDao.upsert(next)
    }

    override suspend fun incrementHabit(habitId: HabitId) {
        val today = todayDate()
        val entity = habitDao.getById(habitId.value) ?: return
        val current = recordDao.getRecord(habitId.value, today)
        val newCount = ((current?.currentCount ?: 0) + 1)
            .coerceAtMost(entity.targetCount ?: Int.MAX_VALUE)
        recordDao.upsert(
            HabitDailyRecordEntity(
                habitId = habitId.value,
                date = today,
                isCompleted = newCount >= (entity.targetCount ?: 1),
                currentCount = newCount,
            ),
        )
    }

    override suspend fun decrementHabit(habitId: HabitId) {
        val today = todayDate()
        val entity = habitDao.getById(habitId.value) ?: return
        val current = recordDao.getRecord(habitId.value, today) ?: return
        val newCount = (current.currentCount - 1).coerceAtLeast(0)
        recordDao.upsert(
            current.copy(
                currentCount = newCount,
                isCompleted = newCount >= (entity.targetCount ?: 1),
            ),
        )
    }

    override suspend fun updateMood(mood: Mood) {
        val today = todayDate()
        val current = reflectionDao.getForDate(today)
        reflectionDao.upsert(
            (current ?: DailyReflectionEntity(today, null, "")).copy(mood = mood.name),
        )
    }

    override suspend fun updateReflectionNote(note: String) {
        val today = todayDate()
        val current = reflectionDao.getForDate(today)
        reflectionDao.upsert(
            (current ?: DailyReflectionEntity(today, null, "")).copy(note = note),
        )
    }

    override suspend fun createHabit(habit: Habit) {
        habitDao.upsert(habit.toEntity())
    }

    override suspend fun updateHabit(habit: Habit) {
        habitDao.update(habit.toEntity())
    }

    override suspend fun deleteHabit(habitId: HabitId) {
        habitDao.deleteById(habitId.value)
    }

    override suspend fun getHabitById(id: HabitId): Habit? =
        habitDao.getById(id.value)?.toDomain()

    suspend fun seedIfEmpty() {
        if (habitDao.count() == 0) {
            habitDao.insertAll(defaultHabits())
        }
    }
}