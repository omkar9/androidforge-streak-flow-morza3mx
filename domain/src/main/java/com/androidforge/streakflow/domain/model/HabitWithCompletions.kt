package com.androidforge.streakflow.domain.model

data class HabitWithCompletions(
    val habit: Habit,
    val completions: List<HabitCompletion>
)