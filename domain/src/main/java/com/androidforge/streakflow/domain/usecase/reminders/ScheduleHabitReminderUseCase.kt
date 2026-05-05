package com.androidforge.streakflow.domain.usecase.reminders

import com.androidforge.streakflow.core.common.Result
import com.androidforge.streakflow.core.notifications.AlarmScheduler
import com.androidforge.streakflow.core.notifications.NotificationHelper
import java.time.LocalTime
import javax.inject.Inject

class ScheduleHabitReminderUseCase @Inject constructor(
    private val alarmScheduler: AlarmScheduler,
    private val notificationHelper: NotificationHelper
) {
    operator fun invoke(habitId: Long, habitName: String, reminderTime: LocalTime): Result<Unit> {
        // Ensure notification channel exists before scheduling alarm
        notificationHelper.createNotificationChannel()
        return alarmScheduler.scheduleAlarm(habitId, habitName, reminderTime)
    }
}