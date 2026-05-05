package com.androidforge.streakflow.domain.usecase.onboarding

import com.androidforge.streakflow.core.common.Result
import com.androidforge.streakflow.domain.repository.SettingsRepository
import javax.inject.Inject

class CompleteOnboardingUseCase @Inject constructor(
    private val settingsRepository: SettingsRepository
) {
    suspend operator fun invoke(): Result<Unit> {
        return settingsRepository.setOnboardingCompleted(true)
    }
}