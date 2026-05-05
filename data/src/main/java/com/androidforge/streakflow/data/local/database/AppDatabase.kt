package com.androidforge.streakflow.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.androidforge.streakflow.core.common.Constants
import com.androidforge.streakflow.data.local.database.converter.DayOfWeekListConverter
import com.androidforge.streakflow.data.local.database.converter.FrequencyTypeConverter
import com.androidforge.streakflow.data.local.database.converter.HabitCompletionStatusConverter
import com.androidforge.streakflow.data.local.database.converter.InstantConverter
import com.androidforge.streakflow.data.local.database.converter.LocalDateConverter
import com.androidforge.streakflow.data.local.database.converter.LocalTimeConverter
import com.androidforge.streakflow.data.local.database.dao.HabitCompletionDao
import com.androidforge.streakflow.data.local.database.dao.HabitDao
import com.androidforge.streakflow.data.local.database.entity.HabitCompletionEntity
import com.androidforge.streakflow.data.local.database.entity.HabitEntity

@Database(
    entities = [
        HabitEntity::class,
        HabitCompletionEntity::class
    ],
    version = Constants.DATABASE_VERSION,
    exportSchema = false
)
@TypeConverters(
    InstantConverter::class,
    LocalDateConverter::class,
    LocalTimeConverter::class,
    FrequencyTypeConverter::class,
    DayOfWeekListConverter::class,
    HabitCompletionStatusConverter::class
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun habitDao(): HabitDao
    abstract fun habitCompletionDao(): HabitCompletionDao
}