package com.androidforge.streakflow.domain.repository

import com.androidforge.streakflow.core.common.Result
import kotlinx.coroutines.flow.Flow

interface SettingsRepository {
    fun hasCompletedOnboarding(): Flow<Result<Boolean>>
    suspend fun setOnboardingCompleted(completed: Boolean): Result<Unit>

    fun getNotificationSettings(): Flow<Result<Boolean>>
    suspend fun setNotificationSettings(enabled: Boolean): Result<Unit>
}