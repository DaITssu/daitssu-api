package com.example.test.domain.course.repository

import com.example.test.domain.course.entity.Calendar
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.time.LocalDateTime
import java.time.Month
import java.time.YearMonth

@Repository
interface CalendarRepository : JpaRepository<Calendar,Long> {
    fun findByDueAtBetween(startDateTime: LocalDateTime, endDateTime: LocalDateTime): List<Calendar>
}