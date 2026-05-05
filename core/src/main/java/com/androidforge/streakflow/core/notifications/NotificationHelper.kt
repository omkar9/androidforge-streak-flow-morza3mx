package com.androidforge.streakflow.core.notifications

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.androidforge.streakflow.R
import com.androidforge.streakflow.core.common.Constants
import com.androidforge.streakflow.MainActivity
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class NotificationHelper @Inject constructor(@ApplicationContext private val context: Context) {

    private val notificationManager: NotificationManagerCompat = NotificationManagerCompat.from(context)

    fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = context.getString(R.string.notification_channel_name)
            val descriptionText = context.getString(R.string.notification_channel_description)
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel(Constants.NOTIFICATION_CHANNEL_ID, name, importance).apply {
                description = descriptionText
            }
            val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            manager.createNotificationChannel(channel)
        }
    }

    fun showHabitReminderNotification(habitId: Long, habitName: String) {
        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            putExtra("habitId", habitId)
        }
        val pendingIntent: PendingIntent = PendingIntent.getActivity(
            context,
            habitId.toInt(), // Use habitId as request code for unique pending intents
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val builder = NotificationCompat.Builder(context, Constants.NOTIFICATION_CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_launcher_foreground) // Use a proper icon
            .setContentTitle(context.getString(R.string.notification_reminder_title, habitName))
            .setContentText(context.getString(R.string.notification_reminder_message))
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .setCategory(NotificationCompat.CATEGORY_REMINDER)

        if (notificationManager.areNotificationsEnabled()) {
            notificationManager.notify(habitId.toInt() + Constants.NOTIFICATION_ID_BASE, builder.build())
        }
    }

    fun cancelNotification(habitId: Long) {
        notificationManager.cancel(habitId.toInt() + Constants.NOTIFICATION_ID_BASE)
    }
}