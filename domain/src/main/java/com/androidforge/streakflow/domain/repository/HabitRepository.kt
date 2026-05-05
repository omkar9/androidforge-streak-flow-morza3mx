package com.androidforge.streakflow.domain.repository

import com.androidforge.streakflow.core.common.Result
import com.androidforge.streakflow.domain.model.Habit
import com.androidforge.streakflow.domain.model.HabitCompletionStatus
import com.androidforge.streakflow.domain.model.HabitWithCompletions
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

interface HabitRepository {
    fun getAllActiveHabitsWithCompletions(): Flow<Result<List<HabitWithCompletions>>>
    fun getHabitWithCompletionsById(habitId: Long): Flow<Result<HabitWithCompletions>>
    fun getActiveHabitsForDate(date: LocalDate): Flow<Result<List<HabitWithCompletions>>>
    suspend fun getHabitById(habitId: Long): Result<Habit>
    suspend fun addHabit(habit: Habit): Result<Habit>
    suspend fun updateHabit(habit: Habit): Result<Unit>
    suspend fun deleteHabit(habitId: Long): Result<Unit>
    suspend fun markHabitCompletion(habitId: Long, date: LocalDate, status: HabitCompletionStatus): Result<Unit>
    suspend fun toggleHabitActiveStatus(habitId: Long, isActive: Boolean): Result<Unit>
}