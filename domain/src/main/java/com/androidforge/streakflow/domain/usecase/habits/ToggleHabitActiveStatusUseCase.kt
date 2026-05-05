package com.androidforge.streakflow.domain.usecase.habits

import com.androidforge.streakflow.core.common.Result
import com.androidforge.streakflow.domain.model.Habit
import com.androidforge.streakflow.domain.repository.HabitRepository
import javax.inject.Inject

class ToggleHabitActiveStatusUseCase @Inject constructor(
    private val habitRepository: HabitRepository
) {
    suspend operator fun invoke(habit: Habit): Result<Unit> {
        return habitRepository.toggleHabitActiveStatus(habit.id, habit.isActive)
    }
}