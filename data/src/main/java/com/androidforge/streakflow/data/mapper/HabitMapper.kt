package com.androidforge.streakflow.data.mapper

import com.androidforge.streakflow.data.local.database.entity.HabitEntity
import com.androidforge.streakflow.data.local.database.relations.HabitWithCompletionsRelation
import com.androidforge.streakflow.domain.model.Habit
import com.androidforge.streakflow.domain.model.HabitWithCompletions

fun HabitEntity.toDomain(): Habit {
    return Habit(
        id = id,
        name = name,
        description = description,
        frequencyType = frequencyType,
        frequencyValue = frequencyValue,
        reminderTime = reminderTime,
        createdAt = createdAt,
        isActive = isActive
    )
}

fun Habit.toEntity(): HabitEntity {
    return HabitEntity(
        id = id,
        name = name,
        description = description,
        frequencyType = frequencyType,
        frequencyValue = frequencyValue,
        reminderTime = reminderTime,
        createdAt = createdAt,
        isActive = isActive
    )
}

fun HabitWithCompletionsRelation.toDomain(): HabitWithCompletions {
    return HabitWithCompletions(
        habit = this.habit.toDomain(),
        completions = this.completions.map { it.toDomain() }
    )
}