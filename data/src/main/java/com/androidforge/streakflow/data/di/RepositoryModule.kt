package com.androidforge.streakflow.data.di

import com.androidforge.streakflow.data.repository.HabitRepositoryImpl
import com.androidforge.streakflow.data.repository.SettingsRepositoryImpl
import com.androidforge.streakflow.domain.repository.HabitRepository
import com.androidforge.streakflow.domain.repository.SettingsRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindHabitRepository(habitRepositoryImpl: HabitRepositoryImpl): HabitRepository

    @Binds
    @Singleton
    abstract fun bindSettingsRepository(settingsRepositoryImpl: SettingsRepositoryImpl): SettingsRepository
}