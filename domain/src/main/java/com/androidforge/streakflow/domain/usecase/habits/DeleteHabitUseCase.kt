package com.androidforge.streakflow.domain.usecase.habits

import com.androidforge.streakflow.core.common.Result
import com.androidforge.streakflow.domain.repository.HabitRepository
import javax.inject.Inject

class DeleteHabitUseCase @Inject constructor(
    private val habitRepository: HabitRepository
) {
    suspend operator fun invoke(habitId: Long): Result<Unit> {
        return habitRepository.deleteHabit(habitId)
    }
}