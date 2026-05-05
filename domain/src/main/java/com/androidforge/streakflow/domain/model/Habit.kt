package com.androidforge.streakflow.domain.model

import java.time.DayOfWeek
import java.time.Instant
import java.time.LocalTime

data class Habit(
    val id: Long = 0L,
    val name: String,
    val description: String?,
    val frequencyType: FrequencyType,
    val frequencyValue: List<DayOfWeek>, // For SPECIFIC_DAYS, e.g., [MONDAY, WEDNESDAY]
    val reminderTime: LocalTime?,
    val createdAt: Instant,
    val isActive: Boolean
)