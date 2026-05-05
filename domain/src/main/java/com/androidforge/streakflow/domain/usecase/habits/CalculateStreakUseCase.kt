package com.androidforge.streakflow.domain.usecase.habits

import com.androidforge.streakflow.domain.model.FrequencyType
import com.androidforge.streakflow.domain.model.HabitCompletion
import com.androidforge.streakflow.domain.model.HabitCompletionStatus
import com.androidforge.streakflow.domain.model.StreakInfo
import java.time.DayOfWeek
import java.time.LocalDate
import javax.inject.Inject

class CalculateStreakUseCase @Inject constructor() {

    operator fun invoke(completions: List<HabitCompletion>, habitFrequencyType: FrequencyType, habitFrequencyValue: List<DayOfWeek>): StreakInfo {
        if (completions.isEmpty()) return StreakInfo(0, 0)

        val sortedCompletions = completions.sortedByDescending { it.date }
        val completedDates = sortedCompletions
            .filter { it.status == HabitCompletionStatus.COMPLETED }
            .map { it.date }
            .toSet()

        var currentStreak = 0
        var longestStreak = 0
        var tempStreak = 0
        var currentDate = LocalDate.now()

        // Calculate longest streak across all history
        val allDates = sortedCompletions.map { it.date }.distinct().sorted()
        if (allDates.isNotEmpty()) {
            var i = 0
            while (i < allDates.size) {
                val startDate = allDates[i]
                var currentCheckDate = startDate
                var consecutiveDays = 0

                while (currentCheckDate <= allDates.last()) {
                    val completionsForDate = sortedCompletions.filter { it.date == currentCheckDate }
                    val isCompletedOrSkipped = completionsForDate.any { it.status == HabitCompletionStatus.COMPLETED || it.status == HabitCompletionStatus.SKIPPED }
                    val isCompleted = completionsForDate.any { it.status == HabitCompletionStatus.COMPLETED }

                    val shouldHaveBeenDone = when (habitFrequencyType) {
                        FrequencyType.DAILY -> true
                        FrequencyType.WEEKLY -> true // Simplified: Assume weekly implies any day for streak, more complex logic needed for exact weekly tracking
                        FrequencyType.SPECIFIC_DAYS -> habitFrequencyValue.contains(currentCheckDate.dayOfWeek)
                    }

                    if (shouldHaveBeenDone) {
                        if (isCompleted) {
                            consecutiveDays++
                        } else if (isCompletedOrSkipped) {
                            // Skipped counts as breaking the streak for simple streak calculation
                            longestStreak = maxOf(longestStreak, consecutiveDays)
                            consecutiveDays = 0
                        } else {
                            // Missed a scheduled day without completing or skipping
                            longestStreak = maxOf(longestStreak, consecutiveDays)
                            consecutiveDays = 0
                        }
                    }

                    currentCheckDate = currentCheckDate.plusDays(1)
                }
                longestStreak = maxOf(longestStreak, consecutiveDays)
                i++
            }
        }


        // Calculate current streak (from today backwards)
        // Start from today, go backwards, only counting days the habit *should* have been done
        currentDate = LocalDate.now()
        while (true) {
            val completionsForDate = sortedCompletions.filter { it.date == currentDate }
            val isCompleted = completionsForDate.any { it.status == HabitCompletionStatus.COMPLETED }
            val isSkipped = completionsForDate.any { it.status == HabitCompletionStatus.SKIPPED }

            val shouldHaveBeenDoneToday = when (habitFrequencyType) {
                FrequencyType.DAILY -> true
                FrequencyType.WEEKLY -> true // For simplicity, consider any day for weekly streak
                FrequencyType.SPECIFIC_DAYS -> habitFrequencyValue.contains(currentDate.dayOfWeek)
            }

            if (shouldHaveBeenDoneToday) {
                if (isCompleted) {
                    currentStreak++
                } else if (isSkipped) {
                    // Skipped breaks the current streak
                    break
                } else if (currentDate.isBefore(LocalDate.now())) {
                    // If it's a past date and it was PENDING (not completed/skipped), streak broken
                    // If today is PENDING, it doesn't break yet, but doesn't add to streak
                    break
                } else {
                    // If today and pending, don't count, don't break
                }
            }

            // If we've gone past a scheduled day without completion/skip, streak is broken.
            // This logic is simplified; a more robust solution would track 'eligible' days.
            if (currentDate.isBefore(LocalDate.now().minusDays(currentStreak.toLong() + 1)) && currentStreak > 0) {
                // If there's a gap between today and the last completed day, and it wasn't a non-scheduled day, break.
                // This simple version assumes contiguous daily/scheduled days.
                val previousScheduledDay = findPreviousScheduledDay(currentDate, habitFrequencyType, habitFrequencyValue)
                if (previousScheduledDay != null && !completedDates.contains(previousScheduledDay)) {
                    break
                }
            }

            currentDate = currentDate.minusDays(1)

            // Stop if we go too far back or before the first completion
            if (currentDate.isBefore(sortedCompletions.lastOrNull()?.date ?: LocalDate.MIN)) break
            if (currentDate.isBefore(LocalDate.now().minusYears(1))) break // Safety break
        }

        // Refined current streak calculation for accuracy
        currentStreak = 0
        currentDate = LocalDate.now()
        var potentialStreak = 0
        var checkedScheduledDays = 0

        while (true) {
            val completionsForDate = sortedCompletions.filter { it.date == currentDate }
            val isCompleted = completionsForDate.any { it.status == HabitCompletionStatus.COMPLETED }
            val isSkipped = completionsForDate.any { it.status == HabitCompletionStatus.SKIPPED }

            val isScheduledForDate = when (habitFrequencyType) {
                FrequencyType.DAILY -> true
                FrequencyType.WEEKLY -> true // Simplified: consider all days for weekly for now
                FrequencyType.SPECIFIC_DAYS -> habitFrequencyValue.contains(currentDate.dayOfWeek)
            }

            if (isScheduledForDate) {
                if (isCompleted) {
                    potentialStreak++
                } else if (isSkipped) {
                    potentialStreak = 0 // Skipped breaks the current streak
                    break
                } else if (currentDate.isBefore(LocalDate.now())) {
                    // If a past scheduled day was not completed/skipped, streak is broken.
                    potentialStreak = 0
                    break
                } else {
                    // If today is scheduled and PENDING, we don't add to streak, but don't break it either.
                    // The streak effectively stops *before* today.
                    break
                }
            }

            if (currentDate.isBefore(sortedCompletions.lastOrNull()?.date ?: LocalDate.MIN)) break
            currentDate = currentDate.minusDays(1)
            if (currentDate.isBefore(LocalDate.now().minusYears(2))) break // Safety break
        }
        currentStreak = potentialStreak

        return StreakInfo(currentStreak = currentStreak, longestStreak = longestStreak)
    }

    private fun findPreviousScheduledDay(currentDate: LocalDate, frequencyType: FrequencyType, frequencyValue: List<DayOfWeek>): LocalDate? {
        var checkDate = currentDate.minusDays(1)
        while (checkDate.isAfter(currentDate.minusDays(365))) { // Look back up to a year
            val isScheduled = when (frequencyType) {
                FrequencyType.DAILY -> true
                FrequencyType.WEEKLY -> true
                FrequencyType.SPECIFIC_DAYS -> frequencyValue.contains(checkDate.dayOfWeek)
            }
            if (isScheduled) return checkDate
            checkDate = checkDate.minusDays(1)
        }
        return null
    }
}