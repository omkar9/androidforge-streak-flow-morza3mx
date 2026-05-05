package com.androidforge.streakflow.domain.usecase.settings

import com.androidforge.streakflow.core.common.Result
import com.androidforge.streakflow.domain.repository.SettingsRepository
import javax.inject.Inject

class SetNotificationSettingsUseCase @Inject constructor(
    private val settingsRepository: SettingsRepository
) {
    suspend operator fun invoke(enabled: Boolean): Result<Unit> {
        return settingsRepository.setNotificationSettings(enabled)
    }
}