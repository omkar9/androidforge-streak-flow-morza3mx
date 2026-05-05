package com.androidforge.streakflow.domain.model

import java.time.LocalDate

data class HabitCompletion(
    val id: Long = 0L,
    val habitId: Long,
    val date: LocalDate,
    val status: HabitCompletionStatus
)