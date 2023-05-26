package com.example.common.util

import java.time.DayOfWeek
import java.time.LocalDate

object DateTimeUtil {
    private val WEEKEND = listOf(DayOfWeek.SATURDAY, DayOfWeek.SUNDAY)

    fun LocalDate.isWeekend() = dayOfWeek in WEEKEND
}
