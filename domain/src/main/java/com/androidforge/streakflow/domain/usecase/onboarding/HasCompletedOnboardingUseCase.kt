package com.androidforge.streakflow.domain.usecase.onboarding

import com.androidforge.streakflow.core.common.Result
import com.androidforge.streakflow.domain.repository.SettingsRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class HasCompletedOnboardingUseCase @Inject constructor(
    private val settingsRepository: SettingsRepository
) {
    operator fun invoke(): Flow<Result<Boolean>> {
        return settingsRepository.hasCompletedOnboarding()
    }
}