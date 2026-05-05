package com.androidforge.streakflow.data.repository

import com.androidforge.streakflow.R
import com.androidforge.streakflow.core.common.Result
import com.androidforge.streakflow.data.local.database.dao.HabitCompletionDao
import com.androidforge.streakflow.data.local.database.dao.HabitDao
import com.androidforge.streakflow.data.mapper.toDomain
import com.androidforge.streakflow.data.mapper.toEntity
import com.androidforge.streakflow.domain.model.Habit
import com.androidforge.streakflow.domain.model.HabitCompletionStatus
import com.androidforge.streakflow.domain.model.HabitWithCompletions
import com.androidforge.streakflow.domain.repository.HabitRepository
import com.androidforge.streakflow.presentation.common.util.UiText
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import java.time.LocalDate
import javax.inject.Inject

class HabitRepositoryImpl @Inject constructor(
    private val habitDao: HabitDao,
    private val habitCompletionDao: HabitCompletionDao
) : HabitRepository {

    override fun getAllActiveHabitsWithCompletions(): Flow<Result<List<HabitWithCompletions>>> = flowWrapper {
        habitDao.getAllActiveHabitsWithCompletions().map { relations ->
            relations.map { it.toDomain() }
        }
    }

    override fun getHabitWithCompletionsById(habitId: Long): Flow<Result<HabitWithCompletions>> = flowWrapper {
        habitDao.getHabitWithCompletions(habitId).map { relation ->
            relation?.toDomain()
        }
    }

    override fun getActiveHabitsForDate(date: LocalDate): Flow<Result<List<HabitWithCompletions>>> = flowWrapper {
        // Convert DayOfWeek to its string representation for Room LIKE query
        val dayOfWeekString = date.dayOfWeek.name
        habitDao.getActiveHabitsForDate(dayOfWeekString).map { relations ->
            relations.map { it.toDomain() }
        }
    }

    override suspend fun getHabitById(habitId: Long): Result<Habit> = suspendWrapper {
        habitDao.getHabitById(habitId)?.toDomain()
    }

    override suspend fun addHabit(habit: Habit): Result<Habit> = suspendWrapper {
        val id = habitDao.insertHabit(habit.toEntity())
        habit.copy(id = id)
    }

    override suspend fun updateHabit(habit: Habit): Result<Unit> = suspendWrapper {
        habitDao.updateHabit(habit.toEntity())
    }

    override suspend fun deleteHabit(habitId: Long): Result<Unit> = suspendWrapper {
        habitCompletionDao.deleteCompletionsForHabit(habitId) // Delete associated completions first
        habitDao.deleteHabit(habitId)
    }

    override suspend fun markHabitCompletion(habitId: Long, date: LocalDate, status: HabitCompletionStatus): Result<Unit> = suspendWrapper {
        habitCompletionDao.markHabitCompletion(habitId, date, status)
    }

    override suspend fun toggleHabitActiveStatus(habitId: Long, isActive: Boolean): Result<Unit> = suspendWrapper {
        val habit = habitDao.getHabitById(habitId)
        if (habit != null) {
            habitDao.updateHabit(habit.copy(isActive = isActive))
        } else {
            throw IllegalStateException("Habit not found with id: $habitId")
        }
    }

    // Helper function to wrap Flow operations with Result
    private fun <T> flowWrapper(block: () -> Flow<T?>): Flow<Result<T>> {
        return try {
            block()
                .onStart { emit(Result.Loading) }
                .map { data ->
                    if (data == null) Result.Error(UiText.StringResource(R.string.error_message_generic)) else Result.Success(data)
                }
                .catch { e ->
                    emit(Result.Error(UiText.DynamicString(e.localizedMessage ?: "Unknown error"), e))
                }
        } catch (e: Exception) {
            kotlinx.coroutines.flow.flowOf(Result.Error(UiText.DynamicString(e.localizedMessage ?: "Unknown error"), e))
        }
    }

    // Helper function to wrap suspend operations with Result
    private suspend fun <T> suspendWrapper(block: suspend () -> T?): Result<T> {
        return try {
            val data = block()
            if (data == null) Result.Error(UiText.StringResource(R.string.error_message_generic)) else Result.Success(data)
        } catch (e: Exception) {
            Result.Error(UiText.DynamicString(e.localizedMessage ?: "Unknown error"), e)
        }
    }
}