package com.androidforge.streakflow.data.local.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.androidforge.streakflow.domain.model.FrequencyType
import java.time.DayOfWeek
import java.time.Instant
import java.time.LocalTime

@Entity(tableName = "habits")
data class HabitEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    val name: String,
    val description: String?,
    val frequencyType: FrequencyType,
    val frequencyValue: List<DayOfWeek>, // Stores days if frequencyType is SPECIFIC_DAYS
    val reminderTime: LocalTime?,
    val createdAt: Instant,
    val isActive: Boolean
)