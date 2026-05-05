package com.androidforge.streakflow.data.local.database.converter

import androidx.room.TypeConverter
import com.androidforge.streakflow.domain.model.HabitCompletionStatus

class HabitCompletionStatusConverter {
    @TypeConverter
    fun fromStatus(value: HabitCompletionStatus?): String? {
        return value?.name
    }

    @TypeConverter
    fun toStatus(value: String?): HabitCompletionStatus? {
        return value?.let { HabitCompletionStatus.valueOf(it) }
    }
}