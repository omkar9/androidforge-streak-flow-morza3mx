package com.androidforge.streakflow.core.notifications

import com.androidforge.streakflow.core.common.Result
import java.time.LocalTime

interface AlarmScheduler {
    fun scheduleAlarm(habitId: Long, habitName: String, time: LocalTime): Result<Unit>
    fun cancelAlarm(habitId: Long): Result<Unit>
    fun rescheduleAllAlarms(): Result<Unit>
}