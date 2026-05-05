package com.androidforge.streakflow.domain.usecase.habits

import com.androidforge.streakflow.core.common.Result
import com.androidforge.streakflow.domain.model.HabitCompletion
import com.androidforge.streakflow.domain.repository.HabitRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetHabitHistoryUseCase @Inject constructor(
    private val habitRepository: HabitRepository
) {
    operator fun invoke(habitId: Long): Flow<Result<List<HabitCompletion>>> {
        return habitRepository.getHabitWithCompletionsById(habitId).map {
            when (it) {
                is Result.Success -> Result.Success(it.data?.completions ?: emptyList())
                is Result.Error -> Result.Error(it.message, it.exception)
                is Result.Loading -> Result.Loading
                is Result.Offline -> Result.Offline
            }
        }
    }
}