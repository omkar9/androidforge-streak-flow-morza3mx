package com.androidforge.streakflow.data.local.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.androidforge.streakflow.data.local.database.entity.HabitCompletionEntity
import com.androidforge.streakflow.domain.model.HabitCompletionStatus
import java.time.LocalDate

@Dao
interface HabitCompletionDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertHabitCompletion(completion: HabitCompletionEntity): Long

    @Update
    suspend fun updateHabitCompletion(completion: HabitCompletionEntity)

    @Query("SELECT * FROM habit_completions WHERE habitId = :habitId AND date = :date")
    suspend fun getHabitCompletionForDate(habitId: Long, date: LocalDate): HabitCompletionEntity?

    @Query("SELECT * FROM habit_completions WHERE habitId = :habitId ORDER BY date DESC")
    fun getCompletionsForHabit(habitId: Long): List<HabitCompletionEntity>

    @Query("DELETE FROM habit_completions WHERE habitId = :habitId")
    suspend fun deleteCompletionsForHabit(habitId: Long)

    // For marking completion status
    @Transaction
    suspend fun markHabitCompletion(habitId: Long, date: LocalDate, status: HabitCompletionStatus) {
        val existingCompletion = getHabitCompletionForDate(habitId, date)
        if (existingCompletion != null) {
            updateHabitCompletion(existingCompletion.copy(status = status))
        } else {
            insertHabitCompletion(HabitCompletionEntity(habitId = habitId, date = date, status = status))
        }
    }
}