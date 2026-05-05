package com.androidforge.streakflow.domain.di

import com.androidforge.streakflow.domain.repository.HabitRepository
import com.androidforge.streakflow.domain.usecase.habits.AddHabitUseCase
import com.androidforge.streakflow.domain.usecase.habits.CalculateStreakUseCase
import com.androidforge.streakflow.domain.usecase.habits.DeleteHabitUseCase
import com.androidforge.streakflow.domain.usecase.habits.GetAllHabitsUseCase
import com.androidforge.streakflow.domain.usecase.habits.GetHabitByIdUseCase
import com.androidforge.streakflow.domain.usecase.habits.GetHabitHistoryUseCase
import com.androidforge.streakflow.domain.usecase.habits.GetHabitsForDateUseCase
import com.androidforge.streakflow.domain.usecase.habits.MarkHabitCompletionUseCase
import com.androidforge.streakflow.domain.usecase.habits.ToggleHabitActiveStatusUseCase
import com.androidforge.streakflow.domain.usecase.habits.UpdateHabitUseCase
import com.androidforge.streakflow.domain.usecase.onboarding.CompleteOnboardingUseCase
import com.androidforge.streakflow.domain.usecase.onboarding.HasCompletedOnboardingUseCase
import com.androidforge.streakflow.domain.usecase.reminders.CancelHabitReminderUseCase
import com.androidforge.streakflow.domain.usecase.reminders.ScheduleHabitReminderUseCase
import com.androidforge.streakflow.domain.usecase.settings.GetNotificationSettingsUseCase
import com.androidforge.streakflow.domain.usecase.settings.SetNotificationSettingsUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object UseCaseModule {

    @Provides
    @Singleton
    fun provideAddHabitUseCase(repository: HabitRepository): AddHabitUseCase {
        return AddHabitUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideUpdateHabitUseCase(repository: HabitRepository): UpdateHabitUseCase {
        return UpdateHabitUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideDeleteHabitUseCase(repository: HabitRepository): DeleteHabitUseCase {
        return DeleteHabitUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideGetHabitByIdUseCase(repository: HabitRepository): GetHabitByIdUseCase {
        return GetHabitByIdUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideGetAllHabitsUseCase(repository: HabitRepository): GetAllHabitsUseCase {
        return GetAllHabitsUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideGetHabitsForDateUseCase(repository: HabitRepository): GetHabitsForDateUseCase {
        return GetHabitsForDateUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideMarkHabitCompletionUseCase(repository: HabitRepository): MarkHabitCompletionUseCase {
        return MarkHabitCompletionUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideCalculateStreakUseCase(): CalculateStreakUseCase {
        return CalculateStreakUseCase()
    }

    @Provides
    @Singleton
    fun provideGetHabitHistoryUseCase(repository: HabitRepository): GetHabitHistoryUseCase {
        return GetHabitHistoryUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideToggleHabitActiveStatusUseCase(repository: HabitRepository): ToggleHabitActiveStatusUseCase {
        return ToggleHabitActiveStatusUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideScheduleHabitReminderUseCase(alarmScheduler: com.androidforge.streakflow.core.notifications.AlarmScheduler, notificationHelper: com.androidforge.streakflow.core.notifications.NotificationHelper): ScheduleHabitReminderUseCase {
        return ScheduleHabitReminderUseCase(alarmScheduler, notificationHelper)
    }

    @Provides
    @Singleton
    fun provideCancelHabitReminderUseCase(alarmScheduler: com.androidforge.streakflow.core.notifications.AlarmScheduler): CancelHabitReminderUseCase {
        return CancelHabitReminderUseCase(alarmScheduler)
    }

    @Provides
    @Singleton
    fun provideGetNotificationSettingsUseCase(repository: com.androidforge.streakflow.domain.repository.SettingsRepository): GetNotificationSettingsUseCase {
        return GetNotificationSettingsUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideSetNotificationSettingsUseCase(repository: com.androidforge.streakflow.domain.repository.SettingsRepository): SetNotificationSettingsUseCase {
        return SetNotificationSettingsUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideCompleteOnboardingUseCase(repository: com.androidforge.streakflow.domain.repository.SettingsRepository): CompleteOnboardingUseCase {
        return CompleteOnboardingUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideHasCompletedOnboardingUseCase(repository: com.androidforge.streakflow.domain.repository.SettingsRepository): HasCompletedOnboardingUseCase {
        return HasCompletedOnboardingUseCase(repository)
    }
}