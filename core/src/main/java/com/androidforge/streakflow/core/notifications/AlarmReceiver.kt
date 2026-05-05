package com.androidforge.streakflow.core.notifications

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.androidforge.streakflow.core.common.Constants
import com.androidforge.streakflow.domain.usecase.settings.GetNotificationSettingsUseCase
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import timber.log.Timber
import java.time.LocalTime
import javax.inject.Inject

@AndroidEntryPoint
class AlarmReceiver : BroadcastReceiver() {

    @Inject
    lateinit var notificationHelper: NotificationHelper

    @Inject
    lateinit var alarmScheduler: AlarmScheduler

    @Inject
    lateinit var getNotificationSettingsUseCase: GetNotificationSettingsUseCase

    override fun onReceive(context: Context?, intent: Intent?) {
        val habitId = intent?.getLongExtra(EXTRA_HABIT_ID, -1L) ?: -1L
        val habitName = intent?.getStringExtra(EXTRA_HABIT_NAME) ?: ""

        if (habitId == -1L || habitName.isBlank()) {
            Timber.e("AlarmReceiver received invalid habitId or habitName")
            return
        }

        CoroutineScope(Dispatchers.IO).launch {
            val notificationSettingsResult = getNotificationSettingsUseCase().first()
            if (notificationSettingsResult is com.androidforge.streakflow.core.common.Result.Success && notificationSettingsResult.data == true) {
                Timber.d("Alarm received for habit: %s (ID: %d). Showing notification.", habitName, habitId)
                notificationHelper.showHabitReminderNotification(habitId, habitName)
            } else {
                Timber.d("Notifications disabled or failed to retrieve settings. Not showing notification for habit: %s (ID: %d)", habitName, habitId)
            }
            // Reschedule the alarm for the next day, as it's a daily reminder
            val now = LocalTime.now()
            alarmScheduler.scheduleAlarm(habitId, habitName, now) // Reschedule for roughly same time tomorrow
        }

        if (intent?.action == Intent.ACTION_BOOT_COMPLETED || intent?.action == Intent.ACTION_MY_PACKAGE_REPLACED) {
            Timber.d("Boot completed or package replaced. Rescheduling all alarms.")
            alarmScheduler.rescheduleAllAlarms()
        }
    }

    companion object {
        const val EXTRA_HABIT_ID = "extra_habit_id"
        const val EXTRA_HABIT_NAME = "extra_habit_name"
    }
}