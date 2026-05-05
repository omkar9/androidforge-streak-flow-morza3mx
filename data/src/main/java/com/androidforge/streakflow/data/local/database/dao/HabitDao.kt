package com.androidforge.streakflow.data.local.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.androidforge.streakflow.data.local.database.entity.HabitEntity
import com.androidforge.streakflow.data.local.database.relations.HabitWithCompletionsRelation
import kotlinx.coroutines.flow.Flow
import java.time.DayOfWeek
import java.time.LocalDate

@Dao
interface HabitDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertHabit(habit: HabitEntity): Long

    @Update
    suspend fun updateHabit(habit: HabitEntity)

    @Query("DELETE FROM habits WHERE id = :habitId")
    suspend fun deleteHabit(habitId: Long)

    @Transaction
    @Query("SELECT * FROM habits WHERE id = :habitId")
    fun getHabitWithCompletions(habitId: Long): Flow<HabitWithCompletionsRelation?>

    @Transaction
    @Query("SELECT * FROM habits WHERE isActive = 1")
    fun getAllActiveHabitsWithCompletions(): Flow<List<HabitWithCompletionsRelation>>

    @Transaction
    @Query("SELECT * FROM habits WHERE id = :habitId")
    suspend fun getHabitWithCompletionsOnce(habitId: Long): HabitWithCompletionsRelation?

    // Query to get habits scheduled for a specific date
    @Transaction
    @Query(
        "SELECT * FROM habits WHERE isActive = 1 AND " +
                "(frequencyType = 'DAILY' OR " +
                "frequencyType = 'WEEKLY' OR " +
                "(frequencyType = 'SPECIFIC_DAYS' AND " +
                "EXISTS (SELECT 1 FROM habits WHERE id = habits.id AND frequencyValue LIKE '%' || :dayOfWeekString || '%')))"
    )
    fun getActiveHabitsForDate(dayOfWeekString: String): Flow<List<HabitWithCompletionsRelation>>

    @Query("SELECT * FROM habits WHERE id = :habitId")
    suspend fun getHabitById(habitId: Long): HabitEntity?
}