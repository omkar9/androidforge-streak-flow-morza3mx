package com.androidforge.streakflow.data.local.database.relations

import androidx.room.Embedded
import androidx.room.Relation
import com.androidforge.streakflow.data.local.database.entity.HabitCompletionEntity
import com.androidforge.streakflow.data.local.database.entity.HabitEntity

data class HabitWithCompletionsRelation(
    @Embedded val habit: HabitEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "habitId"
    )
    val completions: List<HabitCompletionEntity>
)