package com.androidforge.streakflow.data.mapper

import com.androidforge.streakflow.data.local.database.entity.HabitCompletionEntity
import com.androidforge.streakflow.domain.model.HabitCompletion

fun HabitCompletionEntity.toDomain(): HabitCompletion {
    return HabitCompletion(
        id = id,
        habitId = habitId,
        date = date,
        status = status
    )
}

fun HabitCompletion.toEntity(): HabitCompletionEntity {
    return HabitCompletionEntity(
        id = id,
        habitId = habitId,
        date = date,
        status = status
    )
}