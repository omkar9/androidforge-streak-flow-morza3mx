package com.androidforge.streakflow.domain.usecase.habits

import com.androidforge.streakflow.core.common.Result
import com.androidforge.streakflow.domain.model.HabitWithCompletions
import com.androidforge.streakflow.domain.repository.HabitRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetAllHabitsUseCase @Inject constructor(
    private val habitRepository: HabitRepository
) {
    operator fun invoke(): Flow<Result<List<HabitWithCompletions>>> {
        return habitRepository.getAllActiveHabitsWithCompletions()
    }
}