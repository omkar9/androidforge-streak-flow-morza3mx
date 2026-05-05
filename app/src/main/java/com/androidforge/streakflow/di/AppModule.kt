package com.androidforge.streakflow.di

import android.content.Context
import com.androidforge.streakflow.admob.AdMobInitializer
import com.androidforge.streakflow.admob.InterstitialAdManager
import com.androidforge.streakflow.core.notifications.AlarmScheduler
import com.androidforge.streakflow.core.notifications.AlarmSchedulerImpl
import com.androidforge.streakflow.core.notifications.NotificationHelper
import com.androidforge.streakflow.domain.repository.HabitRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideAdMobInitializer(@ApplicationContext context: Context): AdMobInitializer {
        return AdMobInitializer(context)
    }

    @Provides
    @Singleton
    fun provideInterstitialAdManager(@ApplicationContext context: Context): InterstitialAdManager {
        return InterstitialAdManager(context)
    }

    @Provides
    @Singleton
    fun provideNotificationHelper(@ApplicationContext context: Context): NotificationHelper {
        return NotificationHelper(context)
    }

    @Provides
    @Singleton
    fun provideAlarmScheduler(@ApplicationContext context: Context, habitRepository: HabitRepository): AlarmScheduler {
        return AlarmSchedulerImpl(context, habitRepository)
    }
}