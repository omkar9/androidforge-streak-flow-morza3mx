package com.androidforge.streakflow.domain.usecase.reminders

import com.androidforge.streakflow.core.common.Result
import com.androidforge.streakflow.core.notifications.AlarmScheduler
import javax.inject.Inject

class CancelHabitReminderUseCase @Inject constructor(
    private val alarmScheduler: AlarmScheduler
) {
    operator fun invoke(habitId: Long): Result<Unit> {
        return alarmScheduler.cancelAlarm(habitId)
    }
}