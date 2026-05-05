package com.androidforge.streakflow.data.repository

import com.androidforge.streakflow.core.common.Result
import com.androidforge.streakflow.data.local.preferences.AppPreferences
import com.androidforge.streakflow.domain.repository.SettingsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import javax.inject.Inject

class SettingsRepositoryImpl @Inject constructor(
    private val appPreferences: AppPreferences
) : SettingsRepository {

    override fun hasCompletedOnboarding(): Flow<Result<Boolean>> = flowWrapper {
        appPreferences.hasCompletedOnboarding
    }

    override suspend fun setOnboardingCompleted(completed: Boolean): Result<Unit> = suspendWrapper {
        appPreferences.setOnboardingCompleted(completed)
    }

    override fun getNotificationSettings(): Flow<Result<Boolean>> = flowWrapper {
        appPreferences.getNotificationSettings
    }

    override suspend fun setNotificationSettings(enabled: Boolean): Result<Unit> = suspendWrapper {
        appPreferences.setNotificationSettings(enabled)
    }

    private fun <T> flowWrapper(block: () -> Flow<T>): Flow<Result<T>> {
        return block()
            .onStart { emit(Result.Loading) }
            .map { Result.Success(it) }
            .catch { e -> emit(Result.Error(null, e)) }
    }

    private suspend fun <T> suspendWrapper(block: suspend () -> T): Result<T> {
        return try {
            Result.Success(block())
        } catch (e: Exception) {
            Result.Error(null, e)
        }
    }
}