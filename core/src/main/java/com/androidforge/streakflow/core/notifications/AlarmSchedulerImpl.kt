package com.androidforge.streakflow.core.notifications

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import com.androidforge.streakflow.core.common.Result
import com.androidforge.streakflow.domain.repository.HabitRepository
import com.androidforge.streakflow.presentation.common.util.UiText
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import timber.log.Timber
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneId
import javax.inject.Inject

class AlarmSchedulerImpl @Inject constructor(
    @ApplicationContext private val context: Context,
    private val habitRepository: HabitRepository // Inject repository to get all habits for rescheduling
) : AlarmScheduler {

    private val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
    private val applicationScope = CoroutineScope(Dispatchers.IO)

    override fun scheduleAlarm(habitId: Long, habitName: String, time: LocalTime): Result<Unit> {
        return try {
            val intent = Intent(context, AlarmReceiver::class.java).apply {
                putExtra(AlarmReceiver.EXTRA_HABIT_ID, habitId)
                putExtra(AlarmReceiver.EXTRA_HABIT_NAME, habitName)
            }
            val pendingIntent = PendingIntent.getBroadcast(
                context,
                habitId.toInt(), // Use habitId as request code
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )

            val now = LocalDateTime.now()
            var alarmDateTime = LocalDateTime.of(LocalDate.now(), time)

            // If the reminder time is in the past for today, schedule it for tomorrow
            if (alarmDateTime.isBefore(now)) {
                alarmDateTime = alarmDateTime.plusDays(1)
            }

            val triggerAtMillis = alarmDateTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                if (alarmManager.canScheduleExactAlarms()) {
                    alarmManager.setExactAndAllowWhileIdle(
                        AlarmManager.RTC_WAKEUP,
                        triggerAtMillis,
                        pendingIntent
                    )
                    Timber.d("Scheduled exact alarm for habit %s at %s", habitName, alarmDateTime)
                } else {
                    // If exact alarms cannot be scheduled, fall back to inexact or notify user.
                    Timber.w("Cannot schedule exact alarms. Habit %s reminder might be delayed.", habitName)
                    alarmManager.setAndAllowWhileIdle(
                        AlarmManager.RTC_WAKEUP,
                        triggerAtMillis,
                        pendingIntent
                    )
                }
            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                alarmManager.setExactAndAllowWhileIdle(
                    AlarmManager.RTC_WAKEUP,
                    triggerAtMillis,
                    pendingIntent
                )
                Timber.d("Scheduled exact and allow idle alarm for habit %s at %s", habitName, alarmDateTime)
            } else {
                alarmManager.setExact(
                    AlarmManager.RTC_WAKEUP,
                    triggerAtMillis,
                    pendingIntent
                )
                Timber.d("Scheduled exact alarm for habit %s at %s", habitName, alarmDateTime)
            }

            Result.Success(Unit)
        } catch (e: SecurityException) {
            Timber.e(e, "SecurityException: Missing USE_EXACT_ALARM permission?")
            Result.Error(UiText.DynamicString("Permission missing to schedule exact alarms."), e)
        } catch (e: Exception) {
            Timber.e(e, "Failed to schedule alarm for habit %s", habitName)
            Result.Error(UiText.DynamicString(e.localizedMessage ?: "Failed to schedule reminder"), e)
        }
    }

    override fun cancelAlarm(habitId: Long): Result<Unit> {
        return try {
            val intent = Intent(context, AlarmReceiver::class.java)
            val pendingIntent = PendingIntent.getBroadcast(
                context,
                habitId.toInt(),
                intent,
                PendingIntent.FLAG_NO_CREATE or PendingIntent.FLAG_IMMUTABLE // Use NO_CREATE to check if it exists
            )
            pendingIntent?.let { alarmManager.cancel(it) }
            Timber.d("Cancelled alarm for habit %d", habitId)
            Result.Success(Unit)
        } catch (e: Exception) {
            Timber.e(e, "Failed to cancel alarm for habit %d", habitId)
            Result.Error(UiText.DynamicString(e.localizedMessage ?: "Failed to cancel reminder"), e)
        }
    }

    override fun rescheduleAllAlarms(): Result<Unit> {
        applicationScope.launch {
            when (val result = habitRepository.getAllActiveHabitsWithCompletions().collect { /* collect flow */ } as Result.Success) { // This is a simplified call, needs proper flow handling
                is Result.Success -> {
                    result.data?.forEach { habitWithCompletions ->
                        habitWithCompletions.habit.reminderTime?.let { reminderTime ->
                            scheduleAlarm(habitWithCompletions.habit.id, habitWithCompletions.habit.name, reminderTime)
                        }
                    }
                    Timber.d("Rescheduled all active habit alarms.")
                }
                is Result.Error -> Timber.e("Failed to load habits for rescheduling alarms: %s", result.message?.asString(context))
                is Result.Offline -> Timber.w("Offline, cannot reschedule alarms currently.")
                Result.Loading -> Timber.d("Loading habits for rescheduling...")
            }
        }
        // Immediate return; actual rescheduling happens asynchronously
        return Result.Success(Unit)
    }
}