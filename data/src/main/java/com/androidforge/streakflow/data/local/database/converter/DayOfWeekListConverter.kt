package com.androidforge.streakflow.data.local.database.converter

import androidx.room.TypeConverter
import java.time.DayOfWeek

class DayOfWeekListConverter {
    @TypeConverter
    fun fromDayOfWeekList(value: List<DayOfWeek>?): String? {
        return value?.joinToString(",") { it.name }
    }

    @TypeConverter
    fun toDayOfWeekList(value: String?): List<DayOfWeek> {
        return value?.split(",")?.filter { it.isNotBlank() }?.map { DayOfWeek.valueOf(it) } ?: emptyList()
    }
}