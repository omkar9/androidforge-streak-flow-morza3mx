package com.androidforge.streakflow.domain.usecase.habits

import com.androidforge.streakflow.core.common.Result
import com.androidforge.streakflow.domain.model.HabitCompletionStatus
import com.androidforge.streakflow.domain.repository.HabitRepository
import java.time.LocalDate
import javax.inject.Inject

class MarkHabitCompletionUseCase @Inject constructor(
    private val habitRepository: HabitRepository
) {
    suspend operator fun invoke(habitId: Long, date: LocalDate, status: HabitCompletionStatus): Result<Unit> {
        return habitRepository.markHabitCompletion(habitId, date, status)
    }
}