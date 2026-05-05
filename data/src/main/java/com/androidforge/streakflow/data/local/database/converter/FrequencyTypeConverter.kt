package com.androidforge.streakflow.data.local.database.converter

import androidx.room.TypeConverter
import com.androidforge.streakflow.domain.model.FrequencyType

class FrequencyTypeConverter {
    @TypeConverter
    fun fromFrequencyType(value: FrequencyType?): String? {
        return value?.name
    }

    @TypeConverter
    fun toFrequencyType(value: String?): FrequencyType? {
        return value?.let { FrequencyType.valueOf(it) }
    }
}